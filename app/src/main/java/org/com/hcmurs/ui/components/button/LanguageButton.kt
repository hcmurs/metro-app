package org.com.hcmurs.ui.components.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.com.hcmurs.R
import org.com.hcmurs.utils.LanguageManager

@Composable
fun LanguageButton(
    isScrolled: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val currentLang = LanguageManager.getLocale(context)
    val (displayText, flagEmoji) = when (currentLang) {
        "vi" -> stringResource(R.string.vietnamese) to "\uD83C\uDDFB\uD83C\uDDF3" // 🇻🇳
        "en" -> stringResource(R.string.english) to "\uD83C\uDDFA\uD83C\uDDF8"   // 🇺🇸
        else -> stringResource(R.string.vietnamese) to "\uD83C\uDDFB\uD83C\uDDF3"
    }

    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isScrolled)
                Color.White.copy(alpha = 0.9f)
            else
                Color.White.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$flagEmoji $displayText",
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun LanguageButtonPreview() {
    LanguageButton(isScrolled = false, onClick = {})
}