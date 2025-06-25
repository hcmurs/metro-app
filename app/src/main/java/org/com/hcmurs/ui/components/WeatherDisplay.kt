package org.com.hcmurs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherDisplay(
    temperature: Double,
    windSpeed: Double?= null,
    isScrolled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CloudQueue, // Replace with weather icon when available
            contentDescription = "Temperature",
            tint = if (isScrolled) Color.White else Color.White,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$temperature°C",
            color = if (isScrolled) Color.White else Color.White,
            fontSize = 14.sp
        )

        if (windSpeed == null) return@Row // Skip if wind speed is not provided

        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Add, // Replace with wind icon when available
            contentDescription = "Wind speed",
            tint = if (isScrolled) Color.White else Color.Black,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$windSpeed m/s",
            color = if (isScrolled) Color.White else Color.Black,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherDisplayPreview() {
    WeatherDisplay(
        temperature = 30.0,
        windSpeed = 5.0,
        isScrolled = false
    )
}