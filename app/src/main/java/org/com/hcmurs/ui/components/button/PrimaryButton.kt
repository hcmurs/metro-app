package org.com.hcmurs.ui.components.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit) {
    AppButton(
        text = text,
        onClick = onClick,
        backgroundColor = MaterialTheme.colorScheme.primary
    )
}
