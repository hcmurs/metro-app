package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class TicketDetailInfo(
    val type: String,
    val price: String,
    val validity: String,
    val note: String,
    val description: String = ""
)

@Composable
fun HurcLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = org.com.hcmurs.R.drawable.hurc),
        contentDescription = "HURC Logo",
        modifier = modifier
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavHostController,
    ticketType: String = "Vé 1 ngày",
    ticketPrice: String = "40.000 đ"
) {
    // Create ticket detail based on type
    val ticketDetail = remember (ticketType) {
        when (ticketType) {
            "Vé 1 ngày" -> TicketDetailInfo(
                type = "Vé 1 ngày",
                price = "40.000 đ",
                validity = "24h kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé",
                description = "Vé cho phép sử dụng tất cả các tuyến Metro trong 24 giờ"
            )
            "Vé 3 ngày" -> TicketDetailInfo(
                type = "Vé 3 ngày",
                price = "90.000 đ",
                validity = "72h kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé",
                description = "Vé cho phép sử dụng tất cả các tuyến Metro trong 3 ngày liên tiếp"
            )
            "Vé tháng" -> TicketDetailInfo(
                type = "Vé tháng",
                price = "300.000 đ",
                validity = "30 ngày kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé",
                description = "Vé cho phép sử dụng không giới hạn tất cả các tuyến Metro trong 1 tháng"
            )
            "Vé tháng HSSV" -> TicketDetailInfo(
                type = "Vé tháng HSSV",
                price = "150.000 đ",
                validity = "30 ngày kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé. Chỉ dành cho học sinh, sinh viên có thẻ hợp lệ",
                description = "Vé ưu đãi cho học sinh, sinh viên sử dụng trong 1 tháng"
            )
            else -> TicketDetailInfo(
                type = ticketType,
                price = ticketPrice,
                validity = "Theo quy định",
                note = "Vui lòng xem chi tiết tại quầy vé",
                description = "Thông tin chi tiết về vé"
            )
        }
    }
    Scaffold(
        topBar = {
            TicketDetailTopBar(
                title = ticketDetail.type,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Ticket Card
            TicketDetailCard(ticketDetail = ticketDetail)

            Spacer(modifier = Modifier.height(32.dp))

            // Buy Button
            Button (
                onClick = {
                    // Handle purchase logic
                    // You can navigate to payment screen here
                    // navController.navigate("payment_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Text(
                    text = "Mua ngay: ${ticketDetail.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel Button
            OutlinedButton (
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1976D2)
                )
            ) {
                Text(
                    text = "Hủy",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = Color(0xFF1565C0),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton (onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
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
fun TicketDetailCard(ticketDetail: TicketDetailInfo) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // HURC Logo Area
                HurcLogo(modifier = Modifier.size(80.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Ticket Information
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ticket Type
                TicketInfoRow(
                    label = "Loại vé:",
                    value = ticketDetail.type,
                    valueColor = Color(0xFF333333)
                )

                // Validity
                TicketInfoRow(
                    label = "HSD:",
                    value = ticketDetail.validity,
                    valueColor = Color(0xFF333333)
                )

                // Note
                TicketInfoRow(
                    label = "Lưu ý:",
                    value = ticketDetail.note,
                    valueColor = Color(0xFFE53935)
                )

                // Description if available
                if (ticketDetail.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = ticketDetail.description,
                            fontSize = 14.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(12.dp),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Price Highlight
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1976D2).copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Giá: ${ticketDetail.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}
@Composable
fun TicketInfoRow(
    label: String,
    value: String,
    valueColor: Color = Color(0xFF333333)
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = value,
            fontSize = 16.sp,
            color = valueColor,
            lineHeight = 22.sp
        )
    }
}
@Preview(showBackground = true)
@Composable
fun TicketDetailScreenPreview() {
    val navController = rememberNavController()
    TicketDetailScreen(
        navController = navController,
        ticketType = "Vé 1 ngày",
        ticketPrice = "40.000 đ"
    )
}
