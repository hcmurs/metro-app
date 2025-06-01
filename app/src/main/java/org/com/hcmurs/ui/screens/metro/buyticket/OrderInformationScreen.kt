package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

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
    ticketType: String = "Vé 1 ngày",
    ticketPrice: String = "40.000đ"
) {
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }

    // Available payment methods
    val paymentMethods = remember {
        listOf(
            PaymentMethod(
                id = "momo",
                name = "Ví MoMo",
                icon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "MoMo",
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(24.dp)
                    )
                }
            ),
            PaymentMethod(
                id = "zalopay",
                name = "ZaloPay",
                icon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "ZaloPay",
                        tint = Color(0xFF0066CC),
                        modifier = Modifier.size(24.dp)
                    )
                }
            ),
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
            )
        )
    }
    // Create order info based on ticket
    val orderInfo = remember(ticketType) {
        when (ticketType) {
            "Vé 1 ngày" -> OrderInfo(
                ticketType = "Vé 1 ngày",
                unitPrice = "40.000đ",
                quantity = 1,
                totalPrice = "40.000đ",
                validity = "24h kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé"
            )
            "Vé 3 ngày" -> OrderInfo(
                ticketType = "Vé 3 ngày",
                unitPrice = "90.000đ",
                quantity = 1,
                totalPrice = "90.000đ",
                validity = "72h kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé"
            )
            "Vé tháng" -> OrderInfo(
                ticketType = "Vé tháng",
                unitPrice = "300.000đ",
                quantity = 1,
                totalPrice = "300.000đ",
                validity = "30 ngày kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé"
            )
            "Vé tháng HSSV" -> OrderInfo(
                ticketType = "Vé tháng HSSV",
                unitPrice = "150.000đ",
                quantity = 1,
                totalPrice = "150.000đ",
                validity = "30 ngày kể từ thời điểm kích hoạt",
                note = "Tự động kích hoạt sau 30 ngày kể từ ngày mua vé"
            )
            else -> OrderInfo(
                ticketType = ticketType,
                unitPrice = ticketPrice,
                quantity = 1,
                totalPrice = ticketPrice,
                validity = "Theo quy định",
                note = "Vui lòng xem chi tiết tại quầy vé"
            )
        }
    }

    var isTicketInfoExpanded by remember { mutableStateOf(true) }

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
                color = Color(0xFF4A90E2),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { /* Handle terms click */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Button
            Button(
                onClick = {
                    // Handle payment action
                    if (selectedPaymentMethod != null) {
                        // Proceed with payment
                    } else {
                        // Show message to select payment method
                    }
                },
                enabled = selectedPaymentMethod != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPaymentMethod != null)
                        Color(0xFF4CAF50) else Color(0xFF999999),
                    disabledContainerColor = Color(0xFF999999)
                )
            ) {
                Text(
                    text = "Thanh toán: ${orderInfo.totalPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
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
                color = Color(0xFF1565C0),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1565C0)
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
                color = Color(0xFF1565C0)
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
                            tint = Color(0xFF4A90E2),
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
                color = Color(0xFF1565C0)
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
                    color = Color(0xFF1565C0)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF1565C0),
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
        ticketType = "Vé 1 ngày",
        ticketPrice = "40.000đ"
    )
}