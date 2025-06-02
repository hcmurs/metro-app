package org.com.hcmurs.ui.screens.metro.qrcode

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QRCodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val reader = MultiFormatReader()

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }.toIntArray()

        val source = RGBLuminanceSource(image.width, image.height, pixels)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            val result = reader.decode(bitmap)
            onQRCodeScanned(result.text)
        } catch (e: Exception) {
            // No QR code found in this frame
        } finally {
            image.close()
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }}
@Composable
fun TicketValidationScreen(
    ticket: TicketInfo,
    onScanAgain: () -> Unit,
    onBackPressed: () -> Unit
) {
    val isValid = ticket.validUntil > System.currentTimeMillis()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kết Quả Kiểm Tra Vé",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card (
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isValid)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (isValid) "✓ VÉ HỢP LỆ" else "✗ VÉ HẾT HẠN",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isValid)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Mã vé: ${ticket.ticketId.take(8)}...")
                Text("Từ: ${ticket.fromStation}")
                Text("Đến: ${ticket.toStation}")
                Text("Hành khách: ${ticket.passengerName}")
                Text("Loại vé: ${ticket.ticketType}")
                Text("Giá: ${ticket.price.toInt()} VND")
                Text("Mua lúc: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                    Date(ticket.purchaseTime))}")
                Text("Hết hạn: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(ticket.validUntil))}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button (onClick = onScanAgain) {
                Text("Quét Lại")
            }

            Button(onClick = onBackPressed) {
                Text("Quay Lại")
            }
        }
    }
}