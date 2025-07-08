package org.com.hcmurs.ui.screens.scanqr

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import org.com.hcmurs.repositories.apis.ticket.ScanQRResponse
import org.com.hcmurs.ui.components.topbar.ScanQRTopBar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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

    val scanState by viewModel.scanState.collectAsState()
    var showResultDialog by remember { mutableStateOf(false) }
    var scannedResponse by remember { mutableStateOf<ScanQRResponse?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
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
                                        if (isSuccess) {
                                            navController.popBackStack()
                                        }
                                    }) {
                                        Text("OK")
                                    }
                                }
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
                        scannedText = qrCode
                        Log.d("QR_SCANNER", "QR Code Scanned: $qrCode")

                        // Validate QR code format
                        try {
                            val gson = Gson()
                            gson.fromJson(qrCode, ScanQRResponse::class.java)
                            // Valid format - do something with the ticket
                            val response = gson.fromJson(qrCode, ScanQRResponse::class.java)
                            scannedResponse = response

                            // Call the API with scanned data and stationId
                            if (actionType == ActionType.ENTRY) {
                                viewModel.scanTicketEntry(response, stationId)
                            } else {
                                viewModel.scanTicketExit(response, stationId)
                            }
                            showResultDialog = true
                        } catch (e: Exception) {
                            Log.e("QR_SCANNER", "Invalid QR format: ${e.message}")
                            showInvalidQRDialog = true
                        }
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

                    // Bottom info and scanned result
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Place the QR code inside the frame",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )

                        if (scannedText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.9f)
                                )
                            ) {
                                Text(
                                    text = "heheheh: $scannedText",
                                    modifier = Modifier.padding(16.dp),
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                        }

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
                        text = "Camera permission is required",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { launcher.launch(Manifest.permission.CAMERA) }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }
}

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
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, QRCodeAnalyzer(onQRCodeScanned))
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
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

class QRCodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()
    private var lastScannedTime = 0L
    private val scanCooldown = 2000L // 2 seconds cooldown

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastScannedTime > scanCooldown) {
                            barcode.rawValue?.let { value ->
                                Log.d("QR_SCANNER", "QR Code detected: $value")
                                Log.d("QR_SCANNER", "QR Code format: ${barcode.format}")
                                Log.d("QR_SCANNER", "QR Code value type: ${barcode.valueType}")

                                onQRCodeScanned(value)
                                lastScannedTime = currentTime
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
            imageProxy.close()
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
                .background(
                    Color.Transparent
                )
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