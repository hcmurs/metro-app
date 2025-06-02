package org.com.hcmurs.ui.screens.metro.qrcode
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.gson.Gson
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRScannerScreen(
    onBackPressed: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    var scannedTicket by remember { mutableStateOf<TicketInfo?>(null) }
    var isScanning by remember { mutableStateOf(true) }

    if (cameraPermissionState.status.isGranted) {
        if (isScanning) {
            CameraPreview(
                onQRCodeScanned = { qrContent ->
                    try {
                        val gson = Gson()
                        val ticket = gson.fromJson(qrContent, TicketInfo::class.java)
                        scannedTicket = ticket
                        isScanning = false
                    } catch (e: Exception) {
                        // Invalid QR code
                    }
                }
            )
        } else {
            scannedTicket?.let { ticket ->
                TicketValidationScreen(
                    ticket = ticket,
                    onScanAgain = {
                        isScanning = true
                        scannedTicket = null
                    },
                    onBackPressed = onBackPressed
                )
            }
        }
    } else {
        LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Cần quyền truy cập camera để quét QR code")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Cấp quyền")
            }
        }
    }
}