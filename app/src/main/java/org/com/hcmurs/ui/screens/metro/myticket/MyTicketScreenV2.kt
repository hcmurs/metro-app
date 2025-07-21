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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.repositories.apis.order.OrderWithTicketDetails
import org.com.hcmurs.utils.CurrencyManager
import org.com.hcmurs.utils.LanguageManager
import org.com.hcmurs.utils.TranslationHelper
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
    currencyManager: CurrencyManager,
    viewModel: MyTicketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab = remember { mutableStateOf("NOT_USED") }    // PENDING, FAILED, SUCCESSFUL
    val context = LocalContext.current
    val currentLanguage = LanguageManager.getLocale(context)
    val exchangeRate by currencyManager.exchangeRate.collectAsState()

    // Initialize currency manager on first load
    LaunchedEffect(Unit) {
        currencyManager.updateExchangeRate()
    }
    LaunchedEffect(Unit) {
        viewModel.fetchUserOrders()
    }

    val filteredOrders = remember(uiState.orders, selectedTab.value) {
        uiState.orders.filter { order ->
            when (selectedTab.value) {
                "NOT_USED" -> order.ticket?.status.equals("NOT_USED", ignoreCase = true)
                "USED" -> order.ticket?.status.equals("USED", ignoreCase = true)
                "EXPIRED" -> order.ticket?.status.equals("EXPIRED", ignoreCase = true)
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
            TabRow(
                selectedTabIndex = when (selectedTab.value) {
                    "NOT_USED" -> 0
                    "USED" -> 1
                    "EXPIRED" -> 2
                    else -> 0
                },
                containerColor = Color.White,
                contentColor = DarkGreen
            ) {
                Tab(
                    selected = selectedTab.value == "NOT_USED",
                    onClick = { selectedTab.value = "NOT_USED" },
                    text = { Text(stringResource(R.string.not_used)) }
                )
                Tab(
                    selected = selectedTab.value == "USED",
                    onClick = { selectedTab.value = "USED" },
                    text = { Text(stringResource(R.string.used)) }
                )
                Tab(
                    selected = selectedTab.value == "EXPIRED",
                    onClick = { selectedTab.value = "EXPIRED" },
                    text = { Text(stringResource(R.string.expired)) }
                )
            }

            // Nội dung
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
                } else if (uiState.errorMessage != null) {
                    Text(
                        text = stringResource(R.string.error_label, uiState.errorMessage!!),
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                } else if (filteredOrders.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_tickets_in_category),
                        modifier = Modifier.align(Alignment.Center),
                        color = TextSecondaryColor
                    )
                } else {
                    LazyColumn (
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredOrders) { order ->
                            TicketCard(order = order, navController, currencyManager)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketCard(
    order: OrderWithTicketDetails,
    navController: NavController,
    currencyManager: CurrencyManager
) {
    val ticket = order.ticket ?: return //
    val context = LocalContext.current
    val currentLanguage = LanguageManager.getLocale(context)
    
    // Convert price based on current language
    val vndPrice = order.amount.toDouble()
    val convertedPrice = currencyManager.convertPrice(vndPrice, currentLanguage)
    
    // Get localized ticket name
    val localizedTicketName = TranslationHelper.getLocalizedTicketName(ticket.name, currentLanguage)
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
                    painter = painterResource(id = R.drawable.ic_ticket_info),
                    contentDescription = "Ticket Icon",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(12.dp))
                // Tên vé
                Text(
                    text = localizedTicketName,
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
            InfoRow(label = stringResource(R.string.order_code),
                    value = "#${order.ticket.ticketCode}")

            Spacer(Modifier.height(4.dp))

            InfoRow(label = stringResource(R.string.ticket_price_label),
                value = convertedPrice,)

            Spacer(Modifier.height(4.dp))

            InfoRow(label = stringResource(R.string.validity_label),
                    value = "${formatDate(ticket.validFrom)} - ${formatDate(ticket.validUntil)}")


                Spacer(Modifier.height(16.dp))

                if(ticket.status.equals("NOT_USED") || ticket.status.equals("USED")) {
                    Button(
                        onClick = {
                            navController.navigate(Screen.TicketQRCode.createRoute(order.ticket.ticketCode))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text(stringResource(R.string.use_ticket), color = Color.White, fontWeight = FontWeight.Bold)
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
            Text(text = stringResource(R.string.my_ticket),
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

