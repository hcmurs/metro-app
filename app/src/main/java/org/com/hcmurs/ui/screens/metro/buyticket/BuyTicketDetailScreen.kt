package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.repositories.apis.ticket.TicketType
import org.com.hcmurs.ui.components.logo.HurcLogo
import org.com.hcmurs.utils.CurrencyManager
import org.com.hcmurs.utils.LanguageManager
import org.com.hcmurs.utils.TranslationHelper
import org.com.hcmurs.MainActivity

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
fun TicketDetailCard(
    ticketDetail: TicketType,
    currencyManager: CurrencyManager
) {
    val context = LocalContext.current
    val currentLanguage = LanguageManager.getLocale(context)
    val exchangeRate by currencyManager.exchangeRate.collectAsState()
    val isLoadingRate by currencyManager.isLoading.collectAsState()

    // Initialize currency manager on first load
    LaunchedEffect(Unit) {
        currencyManager.updateExchangeRate()
    }

    // Convert price based on current language
    val vndPrice = when (val price = ticketDetail.price) {
        is Number -> price.toDouble()
        else -> 0.0
    }
    val convertedPrice = currencyManager.convertPrice(vndPrice, currentLanguage)

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
                // Tên vé với translation
                Text(
                    text = TranslationHelper.getLocalizedTicketName(ticketDetail.description, currentLanguage),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Các thông tin chi tiết với translation
                val validityText = TranslationHelper.getLocalizedValidity(ticketDetail.validityDuration, currentLanguage)
                TicketInfoRow(
                    label = stringResource(R.string.validity),
                    value = validityText
                )
                Divider(color = Color.Black.copy(alpha = 0.08f))

                val noteText = TranslationHelper.getLocalizedNote(ticketDetail.name, currentLanguage)
                TicketInfoRow(
                    label = stringResource(R.string.note),
                    value = noteText,
                    valueColor = ErrorColor
                )
                Divider(color = Color.Black.copy(alpha = 0.08f))

                val detailedDescription = TranslationHelper.getLocalizedDescription(ticketDetail.name, currentLanguage)
                TicketInfoRow(
                    label = stringResource(R.string.description),
                    value = detailedDescription
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Phần giá vé với currency conversion
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightGreenBackground)
                        .padding(16.dp)
                ) {
                    Column {
                        if (isLoadingRate) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = DarkGreen
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.updating_price),
                                    fontSize = 14.sp,
                                    color = DarkGreen
                                )
                            }
                        }
                        
                        Text(
                            text = "${stringResource(R.string.ticket_price)}: $convertedPrice",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkGreen,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Show exchange rate info for English
                        if (currentLanguage == "en" && exchangeRate > 0 && !isLoadingRate) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "≈ ${ticketDetail.price}",
                                fontSize = 12.sp,
                                color = DarkGreen.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = currencyManager.getExchangeRateDisplay(),
                                fontSize = 10.sp,
                                color = DarkGreen.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
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
    
    // Get CurrencyManager from the calling activity
    val activity = LocalContext.current as? MainActivity
    val currencyManager = activity?.currencyManager 
    
    if (currencyManager == null) {
        // Show error or fallback UI
        Text(
            text = stringResource(R.string.error),
            color = ErrorColor,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    Scaffold(
        topBar = {
            TicketDetailTopBar(
                title = uiState.ticketDetail?.description ?: stringResource(R.string.ticket_detail),
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
                        text = stringResource(R.string.error) + ": ${uiState.errorMessage}",
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
                        TicketDetailCard(
                            ticketDetail = uiState.ticketDetail!!,
                            currencyManager = currencyManager
                        )
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
                                text = stringResource(org.com.hcmurs.R.string.next),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

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
                                text = stringResource(org.com.hcmurs.R.string.cancel),
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
                        text = stringResource(R.string.not_found_ticket),
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







