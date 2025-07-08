package org.com.hcmurs.ui.screens.stationselection

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.com.hcmurs.FareMatrix
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.Station
import org.com.hcmurs.ui.screens.metro.buyticket.FareMatrixViewModel

private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
private val LightGreenBackground = Color(0xFFF1F8E9)
private val TextPrimaryColor = Color(0xFF212121)
private val TextSecondaryColor = Color(0xFF757575)
private val CardBackgroundColor = Color.White
private val DividerColor = Color.Black.copy(alpha = 0.08f)

data class LocalPaymentMethod(
    val id: Int,
    val name: String,
    val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFareInfoScreen(
    navController: NavHostController,
    entryStationId: Int,
    exitStationId: Int,
    fareMatrixViewModel: FareMatrixViewModel,
    stationViewModel: StationSelectionViewModel
) {
    val fareMatrixUiState by fareMatrixViewModel.uiState.collectAsState()
    val stationUiState by stationViewModel.uiState.collectAsState()

    val fareInfo = fareMatrixUiState.calculatedFare?.data
    val entryStation = stationUiState.stations.find { it.stationId == entryStationId }
    val exitStation = stationUiState.stations.find { it.stationId == exitStationId }


    var showPaymentSheet by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    val paymentMethods = listOf(
        LocalPaymentMethod(1,"VNPAY", R.drawable.ic_vnpay),
        LocalPaymentMethod(2,"MoMo",   R.drawable.ic_momo)
    )
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods.first()) }
    val context = LocalContext.current

    LaunchedEffect (key1 = fareMatrixUiState.createOrderResponse, key2 = fareMatrixUiState.createOrderError) {
        val response = fareMatrixUiState.createOrderResponse
        if (response != null) {
            if (response.status == 200 && response.data != null) {
                Toast.makeText(context, "Tạo đơn hàng thành công!", Toast.LENGTH_SHORT).show()

                navController.navigate(Screen.MyTicket.route)
            } else {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
            }
            fareMatrixViewModel.clearCreateOrderStatus()
        }

        val error = fareMatrixUiState.createOrderError
        if (error != null) {
            // Lỗi mạng hoặc lỗi hệ thống
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            fareMatrixViewModel.clearCreateOrderStatus()
        }
    }

    if (showPaymentSheet) {
        PaymentMethodBottomSheet(
            paymentMethods = paymentMethods,
            selectedMethod = selectedPaymentMethod,
            onDismiss = { showPaymentSheet = false },
            onSelectMethod = { method ->
                selectedPaymentMethod = method
                showPaymentSheet = false
            }
        )
    }

    if (showTermsDialog) {
        TermsAndConditionsDialog(onDismiss = { showTermsDialog = false })
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Thông tin đơn hàng", fontWeight = FontWeight.SemiBold, color = DarkGreen) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CardBackgroundColor)
            )
        },
        bottomBar = {
            if (fareInfo != null) {
                PaymentBottomBar(
                    price = fareInfo.price,
                    isLoading = fareMatrixUiState.isCreatingOrder,
                    onPayClick = {
                        // Gọi ViewModel để tạo đơn hàng
                        fareMatrixViewModel.createSingleOrder(
                            fareMatrixId = fareInfo.fareMatrixId,
                            paymentMethodId = selectedPaymentMethod.id
                        )
                    },
                    onTermsClick = { showTermsDialog = true }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LightGreenBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (fareInfo != null && entryStation != null && exitStation != null) {
                PaymentMethodSection(
                    selectedMethod = selectedPaymentMethod,
                    onClick = { showPaymentSheet = true }
                )
                PaymentInfoSection(fare = fareInfo, entryStation = entryStation, exitStation = exitStation)
                TicketDetailsSection(entryStation = entryStation, exitStation = exitStation)
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Đang tải thông tin đơn hàng...")
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodSection(selectedMethod: LocalPaymentMethod, onClick: () -> Unit) {
    Column {
        Text("Phương thức thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimaryColor)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = selectedMethod.iconRes),
                        contentDescription = selectedMethod.name,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(selectedMethod.name, color = TextPrimaryColor, fontWeight = FontWeight.SemiBold)
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = "Select", tint = TextSecondaryColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodBottomSheet(
    paymentMethods: List<LocalPaymentMethod>,
    selectedMethod: LocalPaymentMethod,
    onDismiss: () -> Unit,
    onSelectMethod: (LocalPaymentMethod) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                "Chọn phương thức thanh toán",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            paymentMethods.forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectMethod(method) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = method.iconRes),
                        contentDescription = method.name,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(method.name, modifier = Modifier.weight(1f), fontSize = 16.sp)
                    if (method == selectedMethod) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Selected",
                            tint = PrimaryGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsAndConditionsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Điều khoản dịch vụ", fontWeight = FontWeight.Bold, color = DarkGreen) },
        text = {
            Text(
                "Bằng việc sử dụng dịch vụ, bạn đồng ý tuân thủ tất cả các quy định về vận chuyển hành khách công cộng. " +
                        "Vé đã mua không thể hoàn trả. Vui lòng giữ vé cẩn thận để xuất trình khi có yêu cầu. " +
                        "Mọi hành vi gian lận sẽ bị xử lý theo quy định của pháp luật. " +
                        "Cảm ơn bạn đã sử dụng dịch vụ của Metro.",
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Đã hiểu")
            }
        }
    )
}

@Composable
private fun PaymentBottomBar(
    price: Int,
    onTermsClick: () -> Unit,
    onPayClick : () -> Unit,
    isLoading: Boolean = false
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp)
            .background(CardBackgroundColor)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val annotatedString = buildAnnotatedString {
                append("Bằng việc bấm thanh toán, bạn đồng ý với ")
                pushStringAnnotation(tag = "TERMS", annotation = "TERMS")
                withStyle(
                    style = SpanStyle(
                        color = DarkGreen,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("điều khoản")
                }
                pop()
                append(" của Metro")
            }

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                        .firstOrNull()?.let {
                            onTermsClick()
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                style = LocalTextStyle.current.copy(
                    fontSize = 12.sp,
                    color = TextSecondaryColor,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onPayClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Thanh toán: ${price}đ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }            }
        }
    }
}

@Composable
private fun PaymentInfoSection(fare: FareMatrix, entryStation: Station, exitStation: Station) {
    val routeName = "${entryStation.name} – ${exitStation.name}"

    Column {
        Text("Thông tin thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimaryColor)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(label = "Sản phẩm:", value = "Vé lượt: $routeName")
                Divider(color = DividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Đơn giá:", value = "${fare.price}đ")
                Divider(color = DividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Số lượng:", value = "1")
                Divider(color = DividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Thành tiền:", value = "${fare.price}đ")
                Divider(color = DividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Tổng giá tiền:", value = "${fare.price}đ", isTotal = true)
            }
        }
    }
}

@Composable
private fun TicketDetailsSection(entryStation: Station, exitStation: Station) {
    val routeName = "${entryStation.name} – ${exitStation.name}"

    Column {
        Text("Thông tin vé lượt: $routeName", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimaryColor)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoRow(label = "Loại vé:", value = "Vé lượt")
                InfoRow(label = "HSD:", value = "30 ngày kể từ ngày mua")
                InfoRow(label = "Lưu ý:", value = "Vé sử dụng một lần", valueColor = Color.Red)
                InfoRow(label = "Mô tả:", value = "Vé lượt: $routeName")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, isTotal: Boolean = false, valueColor: Color? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextSecondaryColor,
            fontSize = if (isTotal) 16.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            color = valueColor ?: if (isTotal) DarkGreen else TextPrimaryColor,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}