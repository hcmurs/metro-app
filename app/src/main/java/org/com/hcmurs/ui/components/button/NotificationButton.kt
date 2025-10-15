package org.com.hcmurs.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NotificationButton(
    unreadCount: Int,
    onClick: () -> Unit,
    bellColor: Color = Color.White,
    dotSize: Dp = 10.dp,
    dotOffsetX: Dp = (-8).dp,
    dotOffsetY: Dp = (8).dp
) {
    Box(modifier = Modifier.wrapContentSize()) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Thông báo",
                tint = bellColor
            )
        }

        if (unreadCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = dotOffsetX, y = dotOffsetY)
                    .size(dotSize)
                    .background(color = Color.Red, shape = CircleShape)
            )
        }
    }
}

@Preview
@Composable
fun NotificationButtonPreview() {
    NotificationButton(unreadCount = 5, onClick = {})
}