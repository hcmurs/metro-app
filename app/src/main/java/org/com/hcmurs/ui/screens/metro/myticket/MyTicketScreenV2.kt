package org.com.hcmurs.ui.screens.metro.myticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.repositories.apis.order.OrderWithTicketDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
private val LightGreenBackground = Color(0xFFF1F8E9)
private val TextPrimaryColor = Color(0xFF212121)
private val TextSecondaryColor = Color(0xFF757575)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketScreen(
    navController: NavController,
    viewModel: MyTicketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab = remember { mutableStateOf("PENDING") } // PENDING, COMPLETED, EXPIRED

    // Lọc danh sách vé dựa trên tab đã chọn
    val filteredOrders = remember(uiState.orders, selectedTab.value) {
        uiState.orders.filter { order ->
            when (selectedTab.value) {
                "PENDING" -> order.status.equals("PENDING", ignoreCase = true) || order.status.equals("ACTIVE", ignoreCase = true)
                "COMPLETED" -> order.status.equals("COMPLETED", ignoreCase = true)
                "EXPIRED" -> order.status.equals("EXPIRED", ignoreCase = true)
                else -> false
            }
        }
    }

    Scaffold (
        topBar = { MyTicketTopBar(navController) },
        containerColor = Color.Transparent
    ) { padding ->
        Column (
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(LightGreenBackground)
        ) {
            // Tabs
            TabRow (
                selectedTabIndex = when (selectedTab.value) {
                    "PENDING" -> 0
                    "COMPLETED" -> 1
                    else -> 2
                },
                containerColor = Color.White,
                contentColor = DarkGreen
            ) {
                Tab (selected = selectedTab.value == "PENDING", onClick = { selectedTab.value = "PENDING" }, text = { Text("Đang hoạt động") })
                Tab(selected = selectedTab.value == "COMPLETED", onClick = { selectedTab.value = "COMPLETED" }, text = { Text("Đã sử dụng") })
                Tab(selected = selectedTab.value == "EXPIRED", onClick = { selectedTab.value = "EXPIRED" }, text = { Text("Hết hạn") })
            }

            // Nội dung
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
                } else if (uiState.errorMessage != null) {
                    Text(
                        text = "Lỗi: ${uiState.errorMessage}",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                } else if (filteredOrders.isEmpty()) {
                    Text(
                        text = "Bạn không có vé nào trong mục này",
                        modifier = Modifier.align(Alignment.Center),
                        color = TextSecondaryColor
                    )
                } else {
                    LazyColumn (
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredOrders) { order ->
                            TicketCard(order = order,navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketCard(order: OrderWithTicketDetails,
               navController: NavController) {
    val ticket = order.ticket ?: return //
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row (verticalAlignment = Alignment.CenterVertically) {
                // Icon vé
                Icon(
                    painter = painterResource(id = R.drawable.ic_ticket_info), // Thay bằng icon vé của bạn
                    contentDescription = "Ticket Icon",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(12.dp))
                // Tên vé
                Text(
                    text = order.ticket.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryColor,
                    modifier = Modifier.weight(1f)
                )
                // Giá vé
//                Text(
//                    text = "${order.amount.toInt()}đ",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = DarkGreen
//                )
            }
            Divider(Modifier.padding(vertical = 12.dp), color = LightGreenBackground)
            // Thông tin chi tiết
            InfoRow(label = "Mã đơn hàng:",
                    value = "#${order.orderId}")

            Spacer(Modifier.height(4.dp))

            InfoRow(label = "Tuyến:", value = order.ticket.ticketCode)
            Spacer(Modifier.height(4.dp))



            InfoRow(label = "Trạng thái:",
                    value = order.status.replaceFirstChar { it.uppercase() },
                    valueColor = getStatusColor(order.status))

            Spacer(Modifier.height(4.dp))

            InfoRow(label ="Giá vé",
                value = "${order.amount.toInt()}đ",)

            Spacer(Modifier.height(4.dp))

            InfoRow(label = "Hiệu lực:",
                    value = "${formatDate(ticket.validFrom)} - ${formatDate(ticket.validUntil)}")

            if (order.status.equals("PENDING", ignoreCase = true) || order.status.equals("ACTIVE", ignoreCase = true)) {
                Spacer(Modifier.height(16.dp))
                Button (
                    onClick = {
                        navController.navigate(Screen.TicketQRCode.createRoute(order.ticket.ticketCode))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("SỬ DỤNG VÉ", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {

        val cleanedDateString = dateString.replace(Regex("(\\+|\\-)\\d{2}:(\\d{2})")) {
            "${it.groupValues[1]}${it.groupValues[2]}00"
        }

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        val date: Date = parser.parse(cleanedDateString) ?: return dateString

        val formatter = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()

        formatter.format(date)
    } catch (e: Exception) {
        dateString.take(10)
    }
}

@Composable
private fun InfoRow(label: String, value: String, valueColor: Color = TextPrimaryColor) {
    Row {
        Text(
            text = label,
            color = TextSecondaryColor,
            fontSize = 14.sp,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Hàm helper để lấy màu theo trạng thái
@Composable
private fun getStatusColor(status: String): Color {
    return when (status.uppercase()) {
        "PENDING", "ACTIVE" -> PrimaryGreen
        "COMPLETED" -> DarkGreen
        "EXPIRED", "CANCELLED" -> Color.Red
        else -> TextSecondaryColor
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Vé của tôi",
                color = DarkGreen,
                fontWeight = FontWeight.Bold)
                },
        navigationIcon = {
            IconButton (onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkGreen)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}