package org.com.hcmurs.ui.components.switchentryexit

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.com.hcmurs.ui.theme.GreenPrimary
import org.com.hcmurs.ui.theme.LightOrange

@Composable
fun SwitchEntryExit(selectedAction: String, onActionSelected: (String) -> Unit) {
    val backgroundColor = Color(0xFFE0E0E0)
    val indicatorColor = when (selectedAction) {
        "Entry" -> GreenPrimary
        "Exit" -> LightOrange
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(4.dp) // padding cho indicator không chạm viền
    ) {
        val transition = updateTransition(targetState = selectedAction, label = "ActionSwitch")

        val translationX by transition.animateFloat(label = "Translation") { state ->
            when (state) {
                "Entry" -> 0f
                "Exit" -> 1f
                else -> 0f
            }
        }

        // Indicator chuyển động
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f) // Chiếm 50% width
                .graphicsLayer {
                    // Calculate translation based on container width
                    this.translationX = this.size.width * translationX
                }
                .background(indicatorColor, RoundedCornerShape(20.dp))
                .zIndex(1f)
        )

        // 2 nút Entry / Exit
        Row(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { onActionSelected("Entry") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedAction == "Entry") Color.White else Color.Black
                )
            ) {
                Text("Entry", fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = { onActionSelected("Exit") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedAction == "Exit") Color.White else Color.Black
                )
            ) {
                Text("Exit", fontWeight = FontWeight.Bold)
            }
        }
    }
}