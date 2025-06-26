package org.com.hcmurs.ui.screens.metro.buyticket

import android.widget.Button
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.repositories.apis.ticket.TicketType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import org.com.hcmurs.Screen
import androidx.compose.material3.*

private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
private val LightGreenBackground = Color(0xFFE8F5E9)
private val TextPrimaryColor = Color(0xFF212121)
private val TextSecondaryColor = Color(0xFF757575)
private val ErrorColor = Color(0xFFD32F2F)

data class TicketDetailInfo(
    val type: String,
    val price: String,
    val validity: String,
    val note: String,
    val description: String = ""
)


data class TicketDetailUiState(
    val ticketDetail: TicketType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
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
fun TicketDetailTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = DarkGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DarkGreen
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent // Nền trong suốt để hòa vào gradient
        )
    )
}

@Composable
fun TicketInfoRow(label: String, value: String, valueColor: Color = TextPrimaryColor) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondaryColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            color = valueColor,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TicketDetailCard(ticketDetail: TicketType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header của Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(LightGreenBackground, Color(0xFFF1F8E9))
                        )
                    )
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                HurcLogo(modifier = Modifier.size(72.dp))
            }

            // Phần thông tin chi tiết
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Tên vé
                Text(
                    text = ticketDetail.description,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Các thông tin chi tiết
                val validityText = when (ticketDetail.validityDuration) {
                    "ONE_DAY" -> "24h kể từ thời điểm kích hoạt"
                    "THREE_DAYS" -> "72h kể từ thời điểm kích hoạt"
                    "ONE_WEEK" -> "7 ngày kể từ thời điểm kích hoạt"
                    "ONE_MONTH" -> "30 ngày kể từ thời điểm kích hoạt"
                    "SINGLE" -> "Sử dụng một lần"
                    else -> "Theo quy định"
                }
                TicketInfoRow(label = "Hạn sử dụng:", value = validityText)
                Divider(color = Color.Black.copy(alpha = 0.08f))

                val noteText = when (ticketDetail.name) {
                    "One Day", "Three Days", "One Week", "One Month" -> "Tự động kích hoạt sau 30 ngày kể từ ngày mua."
                    "Student Monthly" -> "Tự động kích hoạt sau 30 ngày. Chỉ dành cho HSSV có thẻ hợp lệ."
                    else -> "Vui lòng xem chi tiết tại quầy vé."
                }
                TicketInfoRow(label = "Lưu ý:", value = noteText, valueColor = ErrorColor)
                Divider(color = Color.Black.copy(alpha = 0.08f))

                val detailedDescription = when (ticketDetail.name) {
                    "One Day" -> "Vé cho phép sử dụng tất cả các tuyến Metro trong 24 giờ."
                    "Three Days" -> "Vé cho phép sử dụng tất cả các tuyến Metro trong 3 ngày."
                    "One Week" -> "Sử dụng không giới hạn tất cả các tuyến Metro trong 7 ngày."
                    "One Month" -> "Sử dụng không giới hạn tất cả các tuyến Metro trong 1 tháng."
                    "Student Monthly" -> "Vé ưu đãi cho học sinh, sinh viên sử dụng trong 1 tháng."
                    else -> "Thông tin chi tiết về vé."
                }
                TicketInfoRow(label = "Mô tả:", value = detailedDescription)

                Spacer(modifier = Modifier.height(24.dp))

                // Phần giá vé
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightGreenBackground)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Giá: ${ticketDetail.price} đ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkGreen,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavHostController,
    viewModel: TicketDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TicketDetailTopBar(
                title = uiState.ticketDetail?.description ?: "Chi tiết vé",
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, LightGreenBackground),
                        startY = 0f,
                        endY = 1500f
                    )
                )
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // LOGIC GỐC: Hiển thị UI dựa trên state
            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = "Lỗi tải chi tiết vé: ${uiState.errorMessage}",
                        color = ErrorColor,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                uiState.ticketDetail != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        TicketDetailCard(ticketDetail = uiState.ticketDetail!!)
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = {

                                uiState.ticketDetail!!.id.let { id ->
                                    navController.navigate(Screen.OrderInfo.createRoute(id))
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                        ) {
                            Text(
                                text = "Tiếp tục",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Nút Hủy
                        OutlinedButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryGreen)
                        ) {
                            Text(
                                text = "Hủy",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                        }

                        /* LOGIC GỐC: Giữ lại phần code đã được comment
                        // Payment selection section...
                        */

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                else -> {
                    Text(
                        text = "Không tìm thấy thông tin vé.",
                        color = TextSecondaryColor,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketDetailScreenPreview() {
    val navController = rememberNavController()
    TicketDetailScreen(navController = navController)
}







