package org.com.hcmurs.ui.screens.metro.myticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MyTicketScreen() {
    val selectedTab = remember { mutableStateOf("unused") }

    Scaffold(
        topBar = { MyTicketTopBar() },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFDEEF4), Color(0xFFE5F4FD))
                    )
                )
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(4.dp)
            ) {
                TabButton(
                    text = "Đang sử dụng",
                    selected = selectedTab.value == "active",
                    onClick = { selectedTab.value = "active" },
                    modifier = Modifier.weight(1f) // Fill available space equally
                )
                Spacer(modifier = Modifier.width(8.dp))
                TabButton(
                    text = "Chưa sử dụng",
                    selected = selectedTab.value == "unused",
                    onClick = { selectedTab.value = "unused" },
                    modifier = Modifier.weight(1f) // Fill available space equally
                )
            }

            Spacer(Modifier.height(40.dp))

            // Empty ticket message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Bạn không có vé nào",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketTopBar() {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Vé của tôi", color = Color(0xFF2D1E66), fontWeight = FontWeight.Bold)
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO: Navigate home */ }) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF2D1E66))
            }
        },
        actions = {
            TextButton(onClick = { /* TODO: Navigate to expired tickets */ }) {
                Text("Hết hạn", color = Color(0xFF2D1E66))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}


@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // Add this parameter with default value
) {
    val backgroundColor = if (selected) Color(0xFF0052A0) else Color.White
    val textColor = if (selected) Color.White else Color(0xFF2D1E66)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
        modifier = modifier.fillMaxHeight() // Use the passed modifier
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun MyTicketScreenPreview() {
    MyTicketScreen()
}