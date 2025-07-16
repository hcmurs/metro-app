package org.com.hcmurs.ui.components.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
@Composable
fun LoginRequiredDialog(
    onDismissRequest: () -> Unit,
    onLoginClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                Icons.Filled.AccountBox,
                contentDescription = "Information Icon",
                tint = DarkGreen
            )
        },
        title = {
            Text(
                text = "Yêu cầu đăng nhập",
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )
        },
        text = {
            Text(
                text = "Vui lòng đăng nhập để sử dụng tính năng này."
            )
        },
        confirmButton = {
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Đăng nhập")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Hủy", color = Color.Gray)
            }
        }
    )
}
