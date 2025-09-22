/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.weather

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDisplay(
    viewModel: WeatherViewModel = hiltViewModel(),
    isScrolled: Boolean,
) {
    val weather = viewModel.weather.value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = Icons.Default.CloudQueue,
            contentDescription = "Temperature",
            tint = if (isScrolled) Color.White else Color.White,
            modifier = Modifier.size(16.dp),
        )

        Text(
            text = weather?.temperature?.let { "$it°C" } ?: "Loading...",
            color = if (isScrolled) Color.White else Color.White,
            fontSize = 14.sp,
        )

        weather?.windSpeed?.let { windSpeed ->
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Wind speed",
                tint = if (isScrolled) Color.White else Color.White,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = "$windSpeed m/s",
                color = if (isScrolled) Color.White else Color.White,
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherDisplayPreview() {
    Row {
        Icon(
            imageVector = Icons.Default.CloudQueue,
            contentDescription = "Temperature",
            tint = Color.White,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = "30.0°C",
            color = Color.White,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Wind speed",
            tint = Color.Black,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = "5.0 m/s",
            color = Color.Black,
            fontSize = 14.sp,
        )
    }
}
