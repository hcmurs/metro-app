package org.com.hcmurs.ui.screens.metro.buyticket

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheetResultCallback
import com.stripe.android.paymentsheet.rememberPaymentSheet
import org.com.hcmurs.Screen

data class OrderInfo(
    val ticketType: String,
    val unitPrice: String,
    val quantity: Int,
    val totalPrice: String,
    val validity: String,
    val note: String
)
data class PaymentMethod(
    val id: String,
    val name: String,
    val icon: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderInfoScreen(
    navController: NavHostController,
    viewModel: OrderInfoViewModel  = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var isTicketInfoExpanded by remember { mutableStateOf(true) }


    val paymentSheet = rememberPaymentSheet(
        paymentResultCallback = object : PaymentSheetResultCallback {
            override fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
                when (paymentSheetResult) {
                    is PaymentSheetResult.Completed -> {
                        viewModel.verifyPaymentSuccess()
                        navController.navigate(Screen.MyTicket.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                    is PaymentSheetResult.Canceled -> {
                        viewModel.verifyPaymentFailed()
                    }
                    is PaymentSheetResult.Failed -> {
                        viewModel.verifyPaymentFailed()
                    }
                }
            }
        }
    )

    LaunchedEffect(uiState.clientSecret) {
        val clientSecret = uiState.clientSecret
        if (!clientSecret.isNullOrBlank()) {
            val configuration = PaymentSheet.Configuration(
                merchantDisplayName = "HCMURS Metro"
            )
            paymentSheet.presentWithPaymentIntent(
                clientSecret,
                configuration
            )
        }
    }

    LaunchedEffect(uiState.processMessage) {
        uiState.processMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearCheckoutStatus()
        }
    }


    // Available payment methods
    val paymentMethods = remember {
        listOf(

            PaymentMethod(
                id = "vnpay",
                name = "VNPay",
                icon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "VNPay",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(24.dp)
                    )
                }
            ), PaymentMethod(
                id = "stripe",
                name = "Stripe",
                icon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "Stripe",
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        )
    }
    // Derive OrderInfo from fetched TicketType
    val orderInfo = remember(uiState.ticketType) {
        val ticket = uiState.ticketType
        if (ticket != null) {
            val validityText = when (ticket.validityDuration) {
                "Vé 1 ngày" -> "24h kể từ thời điểm kích hoạt"
                "Vé 3 ngày" -> "72h kể từ thời điểm kích hoạt"
                "Vé tuần" -> "7 ngày kể từ thời điểm kích hoạt"
                "Vé tháng" -> "30 ngày kể từ thời điểm kích hoạt"
                "Vé đơn" -> "Sử dụng một lần"
                else -> "Theo quy định"
            }
            val noteText = when (ticket.name) {
                "Vé 1 ngày", "Vé 3 ngày", "Vé tuần", "Vé tháng" -> "Tự động kích hoạt sau 30 ngày kể từ ngày mua."
                "Vé sinh viên" -> "Tự động kích hoạt sau 30 ngày. Chỉ dành cho HSSV có thẻ hợp lệ."
                else -> "Vui lòng xem chi tiết tại quầy vé."
            }
            OrderInfo(
                ticketType = ticket.description,
                unitPrice = "${ticket.price}đ",
                quantity = 1,
                totalPrice = "${ticket.price}đ",
                validity = validityText,
                note = noteText
            )
        } else {
            // Default or loading state for OrderInfo
            OrderInfo(
                ticketType = "Đang tải...",
                unitPrice = "0đ",
                quantity = 0,
                totalPrice = "0đ",
                validity = "Đang tải...",
                note = "Đang tải..."
            )
        }
    }


    Scaffold(
        topBar = {
            OrderInfoTopBar(
                title = "Thông tin đơn hàng",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))   // color background
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Payment Method Section
            PaymentMethodSection(
                selectedPaymentMethod = selectedPaymentMethod,
                paymentMethods = paymentMethods,
                onPaymentMethodSelected = { selectedPaymentMethod = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Payment Info Section
            PaymentInfoSection(orderInfo = orderInfo)

            Spacer(modifier = Modifier.height(16.dp))

            // Ticket Info Section
            TicketInfoSection(
                orderInfo = orderInfo,
                isExpanded = isTicketInfoExpanded,
                onExpandClick = { isTicketInfoExpanded = !isTicketInfoExpanded }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Terms Text
            Text(
                text = "Bằng việc bấm thanh toán, bạn đồng ý với điều khoản của Metro",
                fontSize = 12.sp,
                color = Color(0xFF4CAF50),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { /* Handle terms click */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Button
            val currentSelectedMethod = selectedPaymentMethod
            Button(
                onClick = {
                    val paymentMethodId = 2
                    viewModel.startCheckoutFlow(paymentMethodId)
                },
                enabled = !uiState.isProcessing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    disabledContainerColor = Color(0xFF9E9E9E)
                )
            ) {
                if (uiState.isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text(uiState.processMessage ?: "Đang xử lý...", color = Color.White)
                } else {
                    Text(
                        text = "Thanh toán: ${orderInfo.totalPrice}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderInfoTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = Color(0xFF1A237E),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1A237E)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun PaymentMethodSection(
    selectedPaymentMethod: PaymentMethod?,
    paymentMethods: List<PaymentMethod>,
    onPaymentMethodSelected: (PaymentMethod) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Phương thức thanh toán",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A237E)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedPaymentMethod == null) {
                // Show "Select payment method" when none is selected
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Navigate to payment method selection screen
                            // For demo, just select the first method
                            onPaymentMethodSelected(paymentMethods.first())
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = "Payment Method",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Chọn phương thức thanh toán",
                            fontSize = 14.sp,
                            color = Color(0xFF999999)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Arrow Right",
                        tint = Color(0xFF999999),
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                // Show selected payment method
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Navigate to change payment method
                            // For demo, cycle through methods
                            val currentIndex = paymentMethods.indexOf(selectedPaymentMethod)
                            val nextIndex = (currentIndex + 1) % paymentMethods.size
                            onPaymentMethodSelected(paymentMethods[nextIndex])
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        selectedPaymentMethod.icon()
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = selectedPaymentMethod.name,
                            fontSize = 14.sp,
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Change",
                            tint = Color(0xFF999999),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PaymentInfoSection(orderInfo: OrderInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Thông tin thanh toán",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A237E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Product
            PaymentInfoRow(
                label = "Sản phẩm:",
                value = orderInfo.ticketType
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Unit Price
            PaymentInfoRow(
                label = "Đơn giá:",
                value = orderInfo.unitPrice
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity
            PaymentInfoRow(
                label = "Số lượng:",
                value = orderInfo.quantity.toString()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtotal
            PaymentInfoRow(
                label = "Thành tiền:",
                value = orderInfo.totalPrice
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = Color(0xFFE0E0E0))

            Spacer(modifier = Modifier.height(16.dp))

            // Total Price
            PaymentInfoRow(
                label = "Tổng giá tiền:",
                value = orderInfo.totalPrice,
                isTotal = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Final Amount
            PaymentInfoRow(
                label = "Thành tiền:",
                value = orderInfo.totalPrice,
                isTotal = true
            )
        }
    }
}

@Composable
fun PaymentInfoRow(
    label: String,
    value: String,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 15.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Medium else FontWeight.Normal,
            color = Color(0xFF333333)
        )
        Text(
            text = value,
            fontSize = if (isTotal) 15.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Medium else FontWeight.Normal,
            color = Color(0xFF333333)
        )
    }
}

@Composable
fun TicketInfoSection(
    orderInfo: OrderInfo,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandClick() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Thông tin ${orderInfo.ticketType}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A237E)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF1A237E),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Expandable Content
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))

                // Ticket Type
                TicketDetailRow(
                    label = "Loại vé:",
                    value = orderInfo.ticketType
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Validity
                TicketDetailRow(
                    label = "HSD:",
                    value = orderInfo.validity
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Note
                TicketDetailRow(
                    label = "Lưu ý:",
                    value = orderInfo.note,
                    valueColor = Color(0xFFE53935)
                )
            }
        }
    }
}

@Composable
fun TicketDetailRow(
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
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderInfoScreenPreview() {
    val navController = rememberNavController()
    OrderInfoScreen(
        navController = navController,
    )
}