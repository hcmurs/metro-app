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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
fun TicketDetailScreen(
    navController: NavHostController,
    viewModel: TicketDetailViewModel = hiltViewModel()
) {
   // val selectedPayment = remember { mutableStateOf(org.com.hcmurs.payment.PaymentMethod.MoMo) }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TicketDetailTopBar(
                title = uiState.ticketDetail?.description ?: "Chi tiết vé", // Use ticket description as title
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
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

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1565C0))
                    }
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = "Lỗi tải chi tiết vé: ${uiState.errorMessage}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                uiState.ticketDetail != null -> {
                    // Ticket Card
                    val ticketDetail = uiState.ticketDetail!!

                    TicketDetailCard(ticketDetail = ticketDetail)

                    Spacer(modifier = Modifier.height(24.dp))

                    // TODO: Re-enable payment methods if needed
                    // For now, these are commented out as in your provided code
                    // Payment selection section (commented out in your original code)
                    /*
                    Text(
                        text = "Chọn phương thức thanh toán:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    Row (
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PaymentMethod.values().forEach { method ->
                            OutlinedButton(
                                onClick = { selectedPayment.value = method },
                                border = ButtonDefaults.outlinedButtonBorder.takeIf { selectedPayment.value == method },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selectedPayment.value == method) Color(0xFFBBDEFB) else Color.White,
                                    contentColor = Color(0xFF1976D2)
                                )
                            ) {
                                Text(text = method.displayName)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Buy Button
                    Button (
                        onClick = {
                            when (selectedPayment.value) {
                                org.com.hcmurs.payment.PaymentMethod.MoMo -> {
                                    // Gọi API hoặc điều hướng tới MoMo Payment
                                    navController.navigate("momo_payment_screen")
                                }
                                PaymentMethod.VNPay   -> {
                                    // Gọi API hoặc điều hướng tới VNPay Payment
                                    navController.navigate("vnpay_payment_screen")
                                }
                            }

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
                            text = "Mua ngay: ${uiState.ticketDetail.price} đ", // Use price from fetched data
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    */

                    Spacer(modifier = Modifier.height(16.dp))

                    // Cancel Button
                    OutlinedButton(
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
                else -> {
                    Text(
                        text = "Không tìm thấy thông tin vé.",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
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
fun TicketDetailCard(ticketDetail: TicketType) {
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
                    value = ticketDetail.description,
                    valueColor = Color(0xFF333333)
                )
                        // Validity (Mapping from validityDuration string)
                val validityText = when (ticketDetail.validityDuration) {
                    "ONE_DAY" -> "24h kể từ thời điểm kích hoạt"
                    "THREE_DAYS" -> "72h kể từ thời điểm kích hoạt"
                    "ONE_WEEK" -> "7 ngày kể từ thời điểm kích hoạt"
                    "ONE_MONTH" -> "30 ngày kể từ thời điểm kích hoạt"
                    "SINGLE" -> "Sử dụng một lần"
                    else -> "Theo quy định"
                }
                // Validity
                TicketInfoRow(
                    label = "HSD:",
                    value = validityText,
                    valueColor = Color(0xFF333333)
                )
                // Note - This might need to come from the API if available,
                // otherwise keep it static or derived from ticket type name
                val noteText = when (ticketDetail.name) {
                    "One Day", "Three Days", "One Week", "One Month" -> "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé"
                    "Student Monthly" -> "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé. Chỉ dành cho học sinh, sinh viên có thẻ hợp lệ"
                    else -> "Vui lòng xem chi tiết tại quầy vé"
                }
                // Note
                TicketInfoRow(
                    label = "Lưu ý:",
                    value = noteText,
                    valueColor = Color(0xFFE53935)
                )

                // Description (Mapping from ticket.name or description from API)
                val detailedDescription = when (ticketDetail.name) {
                    "One Day" -> "Vé cho phép sử dụng tất cả các tuyến Metro trong 24 giờ"
                    "Three Days" -> "Vé cho phép sử dụng tất cả các tuyến Metro trong 3 ngày liên tiếp"
                    "One Week" -> "Vé cho phép sử dụng không giới hạn tất cả các tuyến Metro trong 7 ngày"
                    "One Month" -> "Vé cho phép sử dụng không giới hạn tất cả các tuyến Metro trong 1 tháng"
                    "Student Monthly" -> "Vé ưu đãi cho học sinh, sinh viên sử dụng trong 1 tháng"
                    "Single" -> "Vé sử dụng một lần cho một lượt đi"
                    else -> "Thông tin chi tiết về vé" // Default or if API provides a dedicated field
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Description if available
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = detailedDescription,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(12.dp),
                        lineHeight = 20.sp
                    )
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
                    text = "Giá: ${ticketDetail.price} đ",
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
        navController = navController
    )
}
