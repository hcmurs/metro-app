package org.com.hcmurs.ui.components.card.station

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.com.hcmurs.Station
import org.com.hcmurs.ui.theme.GreenPrimary

@Composable
fun StationCard(
    station: Station,
    isSelected: Boolean,
    isEnabled: Boolean = true, // MỚI: Thêm tham số isEnabled
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> GreenPrimary.copy(alpha = 0.2f)
        !isEnabled -> Color.LightGray.copy(alpha = 0.5f) // Màu cho card bị vô hiệu hóa
        else -> Color.White
    }
    val contentColor = when {
        isSelected -> GreenPrimary
        !isEnabled -> Color.Gray
        else -> Color.Black
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(enabled = isEnabled, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (isSelected) BorderStroke(2.dp, GreenPrimary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Station ${station.sequenceOrder}",
                    fontSize = 12.sp,
                    color = if (isSelected) GreenPrimary else Color.Gray
                )
                Text(
                    text = station.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = contentColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}