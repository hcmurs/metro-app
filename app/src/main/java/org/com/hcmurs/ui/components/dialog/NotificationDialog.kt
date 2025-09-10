/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.ui.components.dialog

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.com.hcmurs.ui.theme.PrimaryGreen

@Composable
fun NotificationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        text = { Text(text = message, fontSize = 16.sp) },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White,
                ),
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = Color.White,
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationDialogPreview() {
    NotificationDialog(
        title = "Notification Title",
        message = "This is a sample notification message.",
        onDismiss = {},
    )
}
