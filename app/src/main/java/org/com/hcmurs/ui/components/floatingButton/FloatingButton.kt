package org.com.hcmurs.ui.components.floatingButton

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.com.hcmurs.ui.theme.GreenPrimary

@Composable
fun FloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.shadow(8.dp, CircleShape),
    containerColor: Color = GreenPrimary,
    contentColor: Color = Color.White,
    icon: ImageVector = Icons.Default.Leaderboard,
    contentDescription: String = "Floating Action Button",
){
    FloatingActionButton(
        onClick = onClick,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        Icon(icon, contentDescription)
    }
}