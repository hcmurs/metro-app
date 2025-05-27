package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class TicketOption(
    val title: String,
    val price: String,
    val icon: ImageVector = Icons.Default.ConfirmationNumber
)
data class RouteInfo(
    val from: String,
    val to: String,
    val details: String = "Xem chi tiết"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun BuyTicketScreen(navController: NavHostController) {

    Scaffold (
        topBar = {
            BuyTicketTopBar(
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ){
            // Welcome section
            WelcomeCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Hot section
            HotSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Regular tickets
            TicketOptionsSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Student section
            StudentSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Routes section
            RoutesSection()
            Spacer(modifier = Modifier.height(24.dp))
            LongTermTicketSection()
            Spacer(modifier = Modifier.height(24.dp))
            TicketOptionsSection()
            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom navigation
        }

    }
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mua vé",
                    color = Color(0xFF1565C0),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color(0xFF1565C0)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}
@Composable
fun WelcomeCard() {
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Character icon
            Text(
                text = "🦁",
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column {
                Text(
                    text = "Chào mừng, Anh Tú!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
                Text(
                    text = "Bắt đầu các trải nghiệm mới cùng Metro nhé!",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}
@Composable
fun HotSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = "🔥", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Nổi bật",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "🔥", fontSize = 20.sp)
    }
}
@Composable
fun TicketOptionsSection() {
    val ticketOptions = listOf(
        TicketOption("Vé 1 ngày", "40.000 đ"),
        TicketOption("Vé 3 ngày", "90.000 đ"),
        TicketOption("Vé tháng", "300.000 đ")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ticketOptions.forEach { ticket ->
            TicketCard(ticket = ticket)
        }
    }
}

@Composable
fun TicketCard(ticket: TicketOption) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable() { /* Handle ticket selection */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ticket.icon,
                contentDescription = ticket.title,
                tint = Color(0xFF4A90E2),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = ticket.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = ticket.price,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}
@Composable
fun StudentSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Ưu đãi",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Học sinh",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )
        Text(text = "🎒", fontSize = 20.sp, modifier = Modifier.padding(start = 4.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Sinh viên",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )
        Text(text = "🎓", fontSize = 20.sp, modifier = Modifier.padding(start = 4.dp))
    }

    Spacer(modifier = Modifier.height(12.dp))

    TicketCard(
        ticket = TicketOption("Vé tháng HSSV", "150.000 đ")
    )
}

@Composable()
fun LongTermTicketSection(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ){
        Text(text = "🔥", fontSize = 20.sp)

        Text(
            text = " Đừng quên mua vé dài hạn",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "🚆",
            fontSize = 20.sp
        )
    }

}
@Composable
fun RoutesSection() {
    val routes = listOf(
        RouteInfo("Đi từ ga Bến Thành", ""),
        RouteInfo("Đi từ ga Ba Son", "")
    )

    Column {
        routes.forEach { route ->
            RouteCard(route = route)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
@Composable
fun RouteCard(route: RouteInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle route selection */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = route.from,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1565C0)
                )
                if (route.to.isNotEmpty()) {
                    Text(
                        text = route.to,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }

            Text(
                text = "Xem chi tiết",
                fontSize = 14.sp,
                color = Color(0xFF4A90E2),
                modifier = Modifier.clickable { /* Handle details */ }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BuyTicketScreenPreview() {
    val navController = rememberNavController()
    BuyTicketScreen(navController = navController)
}
