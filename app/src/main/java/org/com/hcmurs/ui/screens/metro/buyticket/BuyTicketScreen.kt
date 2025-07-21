package org.com.hcmurs.ui.screens.metro.buyticket

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.FareMatrix
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.repositories.apis.ticket.TicketType
import org.com.hcmurs.ui.screens.login.LoginViewModel
import org.com.hcmurs.utils.CurrencyManager
import org.com.hcmurs.utils.TranslationHelper
import androidx.compose.runtime.setValue

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

private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
private val LightGreenBackground = Color(0xFFE8F5E9)
private val TextPrimaryColor = Color(0xFF212121)
private val TextSecondaryColor = Color(0xFF757575)

// --- TOP BAR ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.buy_ticket),
                color = PrimaryGreen,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Trở về",
                    tint = PrimaryGreen
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        )
    )
}

// --- WELCOME CARD ---
@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.WavingHand,
                contentDescription = "Welcome",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.welcome_message),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.welcome_subtitle),
                    fontSize = 14.sp,
                    color = Color(0xB3FFFFFF) // White with 70% opacity
                )
            }
        }
    }
}

// --- SECTION HEADER ---
@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = DarkGreen,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = TextPrimaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// --- TICKET CARD ---
@Composable
fun TicketCard(
    ticket: TicketType,
    navController: NavHostController,
    viewModel: LoginViewModel,
    currencyManager: CurrencyManager
) {
    var showDialog by remember { mutableStateOf(false) }
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val currentLanguage = org.com.hcmurs.utils.LanguageManager.getLocale(context)
    val exchangeRate by currencyManager.exchangeRate.collectAsState()
    val isLoadingRate by currencyManager.isLoading.collectAsState()

    // Initialize currency manager on first load
    LaunchedEffect(Unit) {
        currencyManager.updateExchangeRate()
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.notification)) },
            text = { Text(stringResource(R.string.student_verification_required)) },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = org.com.hcmurs.ui.screens.metro.account.PrimaryGreen),
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (ticket.name == "Vé đơn") {
                    navController.navigate(Screen.StationSelection.route)
                } else if (ticket.name == "Vé sinh viên") {
                    if (userProfile?.isStudent == true) {
                        navController.navigate(Screen.BuyTicketDetail.createRoute(ticket.id))
                    } else {
                        showDialog = true
                    }
                } else {
                    navController.navigate(Screen.BuyTicketDetail.createRoute(ticket.id))
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightGreenBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = ticket.description,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = TranslationHelper.getLocalizedTicketName(
                            ticket.description,
                            currentLanguage
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryColor
                    )
                    if (ticket.name != "Vé đơn") {
                        // Convert price based on current language
                        val vndPrice = when (val price = ticket.price) {
                            is Number -> price.toDouble()
                            else -> 0.0
                        }
                        val convertedPrice = currencyManager.convertPrice(vndPrice, currentLanguage)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isLoadingRate && currentLanguage == "en") {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    color = TextSecondaryColor,
                                    strokeWidth = 1.dp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = convertedPrice,
                                fontSize = 14.sp,
                                color = TextSecondaryColor
                            )
                        }
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = stringResource(R.string.buy_ticket),
                tint = TextSecondaryColor.copy(alpha = 0.7f)
            )
        }
    }
}

// --- ROUTE CARD ---
@Composable
fun RouteCard(fareMatrix: FareMatrix, currencyManager: CurrencyManager) {
    val context = LocalContext.current
    val currentLanguage = org.com.hcmurs.utils.LanguageManager.getLocale(context)
    val exchangeRate by currencyManager.exchangeRate.collectAsState()
    val isLoadingRate by currencyManager.isLoading.collectAsState()

    // Convert price based on current language
    val vndPrice = fareMatrix.price.toDouble()
    val convertedPrice = currencyManager.convertPrice(vndPrice, currentLanguage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightGreenBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = fareMatrix.name,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${stringResource(R.string.route_label)} ${fareMatrix.name}",
                        color = TextPrimaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isLoadingRate && currentLanguage == "en") {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                color = TextSecondaryColor,
                                strokeWidth = 1.dp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "${stringResource(R.string.price_label)} $convertedPrice",
                            fontSize = 14.sp,
                            color = TextSecondaryColor
                        )
                    }
                }
            }
            Text(
                text = stringResource(R.string.view),
                color = PrimaryGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun TicketOptionsSection(
    navController: NavHostController,
    viewModel: BuyTicketViewModel,
    loginViewModel: LoginViewModel,
    currencyManager: CurrencyManager
) {
    val ticketOptions by viewModel.ticketTypes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    LaunchedEffect(Unit) {
        if (ticketOptions.isEmpty() && !isLoading && errorMessage == null) {
            viewModel.fetchTicketTypes()
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    } else if (errorMessage != null) {
        Text(
            text = stringResource(R.string.error_loading_tickets, errorMessage!!),
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    } else {

        val ticketSingle = ticketOptions.find { it.name == "Vé đơn" }
        val ticketStudent = ticketOptions.find { it.name == "Vé sinh viên" }
        val otherTickets =
            ticketOptions.filterNot { it.name == "Vé đơn" || it.name == "Vé sinh viên" }



        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ticketSingle?.let {
                SectionHeader(
                    title = stringResource(R.string.route_ticket_section),
                    icon = Icons.Default.Route
                )

                TicketCard(
                    ticket = it,
                    navController = navController,
                    viewModel = loginViewModel,
                    currencyManager = currencyManager
                )
            }

            ticketStudent?.let {
                SectionHeader(
                    title = stringResource(R.string.student_ticket_section),
                    icon = Icons.Default.School
                )

                TicketCard(
                    ticket = it,
                    navController = navController,
                    viewModel = loginViewModel,
                    currencyManager = currencyManager
                )
            }

            if (otherTickets.isNotEmpty()) {
                SectionHeader(
                    title = stringResource(R.string.other_tickets_section),
                    icon = Icons.Default.LocalActivity
                )

                otherTickets.forEach { ticket ->
                    TicketCard(
                        ticket = ticket,
                        navController = navController,
                        viewModel = loginViewModel,
                        currencyManager = currencyManager
                    )
                }
            }
        }
    }
}

@Composable
fun RoutesSection(viewModel: FareMatrixViewModel, currencyManager: CurrencyManager) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.fareMatrices.isEmpty() && !uiState.isLoading && uiState.errorMessage == null) {
            viewModel.fetchFareMatrices()
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    } else if (uiState.errorMessage != null) {
        Text(
            text = stringResource(R.string.error_loading_routes, uiState.errorMessage!!),
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            uiState.fareMatrices.forEach { fareMatrix ->
                RouteCard(fareMatrix = fareMatrix, currencyManager = currencyManager)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketScreen(
    navController: NavHostController,
    currencyManager: CurrencyManager,
    buyTicketViewModel: BuyTicketViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { BuyTicketTopBar(onBackClick = { navController.popBackStack() }) },
        containerColor = Color.White
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            WelcomeCard()
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(12.dp))
            TicketOptionsSection(navController, buyTicketViewModel, loginViewModel, currencyManager)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BuyTicketScreenPreview() {
    // Preview shows welcome card only to avoid requiring CurrencyManager
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, LightGreenBackground),
                    startY = 0f,
                    endY = 1500f
                )
            )
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        WelcomeCard()
    }
}






