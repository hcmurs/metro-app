package org.com.hcmurs.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light theme colors (Pastel Green Tones)
val Green80 = Color(0xFFC8E6C9)      // Light Green 80%
val GreenGrey80 = Color(0xFFA5D6A7)  // Soft Green Grey
val Lime80 = Color(0xFFDCEDC8)       // Lime Green 80%

// Dark theme colors (Deep Green Tones)
val Green40 = Color(0xFF388E3C)      // Dark Green
val GreenGrey40 = Color(0xFF4CAF50)  // Medium Green
val Lime40 = Color(0xFF689F38)       // Lime Darker Green
val LightColorScheme = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    secondary = GreenGrey40,
    onSecondary = Color.White,
    background = Green80,
    surface = Lime80,
)

val DarkColorScheme = darkColorScheme(
    primary = Green80,
    onPrimary = Color.Black,
    secondary = Lime40,
    onSecondary = Color.White,
    background = Color(0xFF1B5E20),
    surface = Color(0xFF2E7D32),
)
