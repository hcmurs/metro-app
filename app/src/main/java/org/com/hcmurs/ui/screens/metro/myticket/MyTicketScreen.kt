//package org.com.hcmurs.ui.screens.metro.myticket
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import org.com.hcmurs.Screen
//import org.com.hcmurs.ui.theme.DarkGreen
//import org.com.hcmurs.ui.theme.GreenPrimary
//import org.com.hcmurs.ui.theme.LightBeige
//import org.com.hcmurs.utils.navigateToHome
//
//
//@Composable
//fun MyTicketScreen(navController: NavController) {
//    val selectedTab = remember { mutableStateOf("active") } // Default là "Đang sử dụng"
//
//    val activeTickets = listOf(
//        Ticket("Vé tháng HSSV", "17:12 21/05/2025", "17:12 20/06/2025", false),
//        Ticket("Vé tháng thường", "06:00 01/06/2025", "06:00 30/06/2025", true),
//        Ticket("Vé tuần học sinh", "08:00 27/05/2025", "08:00 03/06/2025", false)
//    )
//
//    Scaffold(
//        topBar = { MyTicketTopBar(navController) },
//        containerColor = Color.Transparent
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(Color(0xFFFDEEF4), Color(0xFFE5F4FD))
//                    )
//                )
//                .padding(horizontal = 16.dp)
//        ) {
//            Spacer(Modifier.height(16.dp))
//
//            // Tabs
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(40.dp)
//                    .background(Color.White, shape = RoundedCornerShape(20.dp))
//                    .padding(4.dp)
//            ) {
//                TabButton(
//                    text = "Đang sử dụng",
//                    selected = selectedTab.value == "active",
//                    onClick = { selectedTab.value = "active" },
//                    modifier = Modifier.weight(1f)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                TabButton(
//                    text = "Chưa sử dụng",
//                    selected = selectedTab.value == "unused",
//                    onClick = { selectedTab.value = "unused" },
//                    modifier = Modifier.weight(1f)
//                )
//            }
//
//            Spacer(Modifier.height(24.dp))
//
//            if (selectedTab.value == "active") {
//                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                    activeTickets.forEach {
//                        TicketCard(ticket = it)
//                    }
//                }
//            } else {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Text(
//                        text = "Bạn không có vé nào",
//                        color = Color.Gray,
//                        fontSize = 16.sp
//                    )
//                }
//            }
//        }
//    }
//}
//
//data class Ticket(
//    val title: String,
//    val startTime: String,
//    val endTime: String,
//    val isConnected: Boolean
//)
//
//@Composable
//fun TicketCard(ticket: Ticket) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White, shape = RoundedCornerShape(16.dp))
//            .padding(16.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Box(
//                modifier = Modifier
//                    .width(40.dp)
//                    .height(40.dp)
//                    .background(Color(0xFFECF4FB), shape = RoundedCornerShape(8.dp)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("🎫", fontSize = 18.sp)
//            }
//            Spacer(modifier = Modifier.width(12.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Text(ticket.title, fontWeight = FontWeight.Bold, color = Color(0xFF2D1E66))
//                Text(
//                    text = "HSD: ${ticket.startTime} - ${ticket.endTime}",
//                    color = Color.Red,
//                    fontSize = 10.sp
//                )
//            }
//            Spacer(modifier = Modifier.width(12.dp))
//            Text(
//                text = if (ticket.isConnected) "Đã kết nối" else "Chưa kết nối",
//                color = Color.Gray,
//                fontSize = 12.sp
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTicketTopBar(navController: NavController) {
//    TopAppBar(
//        title = {
//            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                Text(text = "Vé của tôi", color = Color(0xFF2D1E66), fontWeight = FontWeight.Bold)
//            }
//        },
//        navigationIcon = {
//            IconButton(onClick = { navigateToHome(navController) }) {
//                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF2D1E66))
//            }
//        },
//        actions = {
//            TextButton(onClick = { /* TODO: Navigate to expired tickets */ }) {
//                Text("Hết hạn", color = Color(0xFF2D1E66))
//            }
//        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.Transparent
//        )
//    )
//}
//
//
//@Composable
//fun TabButton(
//    text: String,
//    selected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier // Add this parameter with default value
//) {
//    val backgroundColor = if (selected) GreenPrimary else Color.White
//    val textColor = if (selected) Color.White else Color(0xFF2D1E66)
//
//    Button(
//        onClick = onClick,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = backgroundColor,
//            contentColor = textColor
//        ),
//        shape = RoundedCornerShape(20.dp),
//        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
//        modifier = modifier.fillMaxHeight() // Use the passed modifier
//    ) {
//        Text(text = text)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MyTicketScreenPreview() {
//    MyTicketScreen(navController = rememberNavController())
//}