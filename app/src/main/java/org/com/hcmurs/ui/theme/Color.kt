/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// val GreenPrimary = Color(0xFF347433)
val PrimaryGreen = Color(0xFF4CAF50)
val SecondaryGreen = Color(0xFF66BB6A)
val DarkGreen = Color(0xFF388E3C)
val LightGreen = Color(0xFFE8F5E8)
val LightGreenBackground = Color(0xFFE8F5E9)
val TextPrimaryColor = Color(0xFF212121)
val TextSecondaryColor = Color(0xFF757575)
val LightOrange = Color(0xFFFF6F3C)
val LightYellow = Color(0xFFFFC107)
val LightBeige = Color(0xFFB4D2BA)
val PaleYellow = Color(0xFFDCE2AA)
val EarthBrown = Color(0xFFB57F50)
val ErrorRed = Color(0xFFD32F2F)
val BackgroundGray = Color(0xFFF8F9FA)
val BorderColor = Color(0xFFE0E0E0)
val PureWhite = Color(0xFFFFFFFF)
val PureBlack = Color(0xFF000000)
val SuccessGreen = Color(0xFF10B981)
val LightGray = Color(0xFFF0F0F0)

val LightColorScheme =
    lightColorScheme(
        primary = PrimaryGreen,
        onPrimary = Color.White,
        background = Color.White,
        onBackground = DarkGreen,
        surface = LightBeige,
        onSurface = DarkGreen,
        secondary = EarthBrown,
        onSecondary = Color.White,
    )

val DarkColorScheme =
    darkColorScheme(
        primary = PrimaryGreen,
        onPrimary = Color.Black,
        secondary = EarthBrown,
        onSecondary = Color.White,
        background = Color.White,
        surface = LightBeige,
    )
