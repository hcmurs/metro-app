package org.com.hcmurs.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.com.hcmurs.ui.screens.metro.account.DarkGreen
import org.com.hcmurs.ui.screens.metro.account.PrimaryGreen

@Composable
fun LoginRequiredDialog(
    onDismissRequest: () -> Unit,
    onLoginClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(androidx.compose.ui.res.stringResource(org.com.hcmurs.R.string.cancel), color = Color.Gray)
                }

                Button(
                    onClick = onLoginClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Đăng nhập", color = Color.White)
                }
            }
        },
        title = {
            Text(
                text = "Yêu cầu đăng nhập",
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Vui lòng đăng nhập để sử dụng tính năng này.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}



@Preview(showBackground = true)
@Composable
fun LoginRequiredDialogPreview() {
    LoginRequiredDialog(
        onDismissRequest = {},
        onLoginClick = {}
    )
}