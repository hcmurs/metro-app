package org.com.hcmurs.ui.screens.metro.qrcode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

data class TicketInfo(
    val ticketId: String,
    val fromStation: String,
    val toStation: String,
    val price: Double,
    val purchaseTime: Long,
    val validUntil: Long,
    val passengerName: String,
    val ticketType: String // "single", "return", "monthly"
)

class QRGenerator {
    fun generateQRCode(ticketInfo: TicketInfo): Bitmap? {
        // Placeholder for QR code generation logic
        // You can use libraries like ZXing or QRGen to generate QR codes
        return try {
            val gson = Gson()
            val ticketJson = gson.toJson(ticketInfo)
            val writer = QRCodeWriter()
            val bitMatrix: BitMatrix = writer.encode(ticketJson, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap

        } catch (e: Exception) {
            null // Handle any exceptions that may occur during QR code generation
        }
    }
}