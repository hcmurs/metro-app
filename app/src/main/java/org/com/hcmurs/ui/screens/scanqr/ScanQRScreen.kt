package org.com.hcmurs.ui.screens.scanqr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import org.com.hcmurs.repositories.apis.ticket.ScanQRResponse
import org.com.hcmurs.ui.components.topbar.ScanQRTopBar
import org.com.hcmurs.utils.vibrateOnError
import org.com.hcmurs.utils.vibrateOnSuccess
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.com.hcmurs.R

enum class ActionType {
    ENTRY, EXIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanQRScreen(
    navController: NavController,
    stationId: Int = 0,
    stationName: String = "",
    viewModel: ScanQRViewModel = hiltViewModel(),
    actionType: ActionType
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var scannedText by remember { mutableStateOf("") }
    var showInvalidQRDialog by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    val scanState by viewModel.scanState.collectAsState()
    var showResultDialog by remember { mutableStateOf(false) }
    var scannedResponse by remember { mutableStateOf<ScanQRResponse?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(scanState) {
        when (scanState) {
            is ScanQRViewModel.ScanState.Success -> {
                // Vibrate on successful scan only
                vibrateOnSuccess(context)
                showResultDialog = true
            }
            is ScanQRViewModel.ScanState.Error -> {
                vibrateOnError(context)
                showResultDialog = true
            }
            else -> { /* No action needed */ }
        }
    }

    LaunchedEffect(showInvalidQRDialog) {
        if (showInvalidQRDialog) {
            // Vibrate on invalid QR code
            vibrateOnError(context)
        }
    }

    if (showInvalidQRDialog) {
        AlertDialog(
            onDismissRequest = { showInvalidQRDialog = false },
            title = { Text("Invalid QR Code") },
            text = { Text("The scanned QR code is not supported. Please scan a valid metro ticket QR code.") },
            confirmButton = {
                TextButton(onClick = { showInvalidQRDialog = false }) {
                    Text("OK")
                }
            },
            containerColor = Color.White,
            titleContentColor = Color.Black,
            textContentColor = Color.DarkGray
        )
    }

    Scaffold(
        topBar = {
            ScanQRTopBar(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black.copy(alpha = 0.9f))
        ) {
            if (hasCameraPermission) {

                when (val state = scanState) {
                    is ScanQRViewModel.ScanState.Success, is ScanQRViewModel.ScanState.Error -> {
                        if (showResultDialog) {
                            val isSuccess = state is ScanQRViewModel.ScanState.Success
                            val message = when (state) {
                                is ScanQRViewModel.ScanState.Success -> state.message
                                is ScanQRViewModel.ScanState.Error -> state.message
                                else -> ""
                            }

                            AlertDialog(
                                onDismissRequest = {
                                    showResultDialog = false
                                    isProcessing = false
                                    if (isSuccess) {
                                        navController.popBackStack()
                                    }
                                },
                                title = { Text(if (isSuccess) "Success" else "Error") },
                                text = {
                                    Column {
                                        Text(message)
                                        if (isSuccess && scannedResponse != null) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Ticket: ${scannedResponse?.ticketTypeName}")
                                            Text("Name: ${scannedResponse?.name}")
                                            Text("Station: $stationName")
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showResultDialog = false
                                        isProcessing = false
                                        if (isSuccess) {
                                            navController.popBackStack()
                                        }
                                    }) {
                                        Text("OK")
                                    }
                                },
                                // Apply different container colors based on success/error
                                containerColor = if (isSuccess) Color.White else Color(0xFFFBE9E7), // Light red for error
                                titleContentColor = if (isSuccess) Color.Black else Color(0xFFD32F2F), // Red for error title
                                textContentColor = if (isSuccess) Color.DarkGray else Color(0xFFD32F2F), // Red for error text
                                iconContentColor = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFD32F2F) // Green for success, red for error
                            )
                        }
                    }

                    is ScanQRViewModel.ScanState.Loading -> {
                        // Show loading indicator if needed
                    }

                    else -> { /* Idle - no dialog */
                    }
                }

                CameraPreview(
                    onQRCodeScanned = { qrCode ->
                        handleQRCodeScanned(
                            qrCode = qrCode,
                            isProcessing = isProcessing,
                            onStartProcessing = { isProcessing = true },
                            onScannedTextChange = { scannedText = it },
                            onScannedResponseChange = { scannedResponse = it },
                            onShowResultDialog = { showResultDialog = true },
                            onShowInvalidDialog = { showInvalidQRDialog = true },
                            onStopProcessing = { isProcessing = false },
                            actionType = actionType,
                            stationId = stationId,
                            viewModel = viewModel
                        )
                    },
                    lifecycleOwner = lifecycleOwner
                )

                // Overlay UI
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Scanning frame overlay
                    Box(
                        modifier = Modifier
                            .size(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Corner brackets
                        ScanningFrame()
                    }

                    // Bottom info and controls
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.place_qr_code),
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )

//                        if (scannedText.isNotEmpty()) {
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Card(
//                                modifier = Modifier.padding(horizontal = 16.dp),
//                                colors = CardDefaults.cardColors(
//                                    containerColor = Color.White.copy(alpha = 0.9f)
//                                )
//                            ) {
//                                Text(
//                                    text = "Scanned: $scannedText",
//                                    modifier = Modifier.padding(16.dp),
//                                    fontSize = 12.sp,
//                                    color = Color.Black
//                                )
//                            }
//                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            } else {
                // Permission denied UI
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Camera Permission",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.camera_permission_required),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
                    ) {
                        Text(stringResource(R.string.grant_permission))
                    }
                }
            }
        }
    }
}

// Helper function to handle QR code processing
private fun handleQRCodeScanned(
    qrCode: String,
    isProcessing: Boolean,
    onStartProcessing: () -> Unit,
    onScannedTextChange: (String) -> Unit,
    onScannedResponseChange: (ScanQRResponse?) -> Unit,
    onShowResultDialog: () -> Unit,
    onShowInvalidDialog: () -> Unit,
    onStopProcessing: () -> Unit,
    actionType: ActionType,
    stationId: Int,
    viewModel: ScanQRViewModel
) {
    if (!isProcessing) {
        onStartProcessing()
        onScannedTextChange(qrCode)
        Log.d("QR_SCANNER", "QR Code Scanned: $qrCode")

        // Validate QR code format
        try {
            val gson = Gson()
            val response = gson.fromJson(qrCode, ScanQRResponse::class.java)
            if (response != null) {
                onScannedResponseChange(response)

                // Call the API with scanned data and stationId
                if (actionType == ActionType.ENTRY) {
                    viewModel.scanTicketEntry(response, stationId)
                } else {
                    viewModel.scanTicketExit(response, stationId)
                }
//                onShowResultDialog()
            } else {
                throw Exception("Invalid QR format")
            }
        } catch (e: Exception) {
            Log.e("QR_SCANNER", "Invalid QR format: ${e.message}")
            onShowInvalidDialog()
            onStopProcessing()
        }
    }
}

// Function to process image from URI
private fun processImageFromUri(
    context: Context,
    imageUri: Uri,
    onQRCodeScanned: (String) -> Unit,
    onError: (String) -> Unit
) {
    try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        if (bitmap != null) {
            val image = InputImage.fromBitmap(bitmap, 0)

            // Enhanced barcode scanner options for better detection
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC,
                    Barcode.FORMAT_DATA_MATRIX,
                    Barcode.FORMAT_PDF417,
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_CODE_39,
                    Barcode.FORMAT_CODE_93,
                    Barcode.FORMAT_CODABAR,
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_EAN_8,
                    Barcode.FORMAT_ITF,
                    Barcode.FORMAT_UPC_A,
                    Barcode.FORMAT_UPC_E
                )
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        barcodes.firstOrNull()?.rawValue?.let { value ->
                            Log.d("QR_SCANNER", "QR Code from image: $value")
                            onQRCodeScanned(value)
                        }
                    } else {
                        onError("No QR code found in the image")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("QR_SCANNER", "QR Code scanning from image failed", exception)
                    onError("Failed to scan QR code from image: ${exception.message}")
                }
        } else {
            onError("Failed to decode image")
        }
    } catch (e: Exception) {
        Log.e("QR_SCANNER", "Error processing image", e)
        onError("Error processing image: ${e.message}")
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    onQRCodeScanned: (String) -> Unit,
    lifecycleOwner: LifecycleOwner
) {
    val context = LocalContext.current
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .apply {
                        setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    // Enhanced resolution for better QR detection
                    .setTargetResolution(android.util.Size(1920, 1080))
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, QRCodeAnalyzer(onQRCodeScanned))
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                    camera.cameraControl.enableTorch(false) // Disable torch by default
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
}

@androidx.camera.core.ExperimentalGetImage
class QRCodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    // Enhanced barcode scanner options for better detection
    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
        )
        .build()

    private val scanner = BarcodeScanning.getClient(options)
    private var lastScannedTime = 0L
    private val scanCooldown = 1500L // Increased to 2 seconds for better stability

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            // Create input image with proper rotation
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastScannedTime > scanCooldown) {
                            barcodes.firstOrNull()?.rawValue?.let { value ->
                                Log.d("QR_SCANNER", "QR Code detected: $value")
                                Log.d("QR_SCANNER", "QR Code format: ${barcodes.first().format}")
                                Log.d("QR_SCANNER", "Image rotation: ${imageProxy.imageInfo.rotationDegrees}")
                                Log.d("QR_SCANNER", "Image size: ${mediaImage.width}x${mediaImage.height}")

                                // Additional validation for screen scanning
                                if (isValidQRContent(value)) {
                                    onQRCodeScanned(value)
                                    lastScannedTime = currentTime
                                } else {
                                    Log.w("QR_SCANNER", "Invalid QR content format")
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("QR_SCANNER", "QR Code scanning failed", exception)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            Log.w("QR_SCANNER", "MediaImage is null")
            imageProxy.close()
        }
    }

    private fun isValidQRContent(content: String): Boolean {
        return try {
            // Check if it's valid JSON (your expected format)
            val gson = Gson()
            val response = gson.fromJson(content, ScanQRResponse::class.java)
            response != null && response.toString().isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }
}

@Composable
fun ScanningFrame() {
    Box(
        modifier = Modifier
            .size(280.dp)
    ) {
        // Top-left corner
        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(3.dp)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .background(Color.White)
            )
        }

        // Top-right corner
        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
        ) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(3.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.White)
            )
        }

        // Bottom-left corner
        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.BottomStart)
        ) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(3.dp)
                    .align(Alignment.BottomStart)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .align(Alignment.BottomStart)
                    .background(Color.White)
            )
        }

        // Bottom-right corner
        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.BottomEnd)
        ) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(3.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScanQRScreenPreview() {
    ScanQRScreen(navController = rememberNavController(), stationId = 1, stationName = "Ben Thanh", actionType = ActionType.ENTRY)
}