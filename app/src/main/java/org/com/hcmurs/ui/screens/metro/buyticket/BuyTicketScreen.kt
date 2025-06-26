package org.com.hcmurs.ui.screens.metro.buyticket

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
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.Screen
import org.com.hcmurs.repositories.apis.ticket.TicketType
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import org.com.hcmurs.FareMatrix

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketScreen(
    navController: NavHostController,
    buyTicketViewModel: BuyTicketViewModel = hiltViewModel(),
    fareMatrixViewModel: FareMatrixViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            BuyTicketTopBar(
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE8F5E8), Color(0xFFFFFFFF)) // Light green to white
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Welcome section
            WelcomeCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Hot section
            HotSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Regular tickets
            TicketOptionsSection(navController, buyTicketViewModel)

            Spacer(modifier = Modifier.height(24.dp))

            // Student section
            StudentSection(navController)

            Spacer(modifier = Modifier.height(24.dp))

            // Routes section
            RoutesSection(fareMatrixViewModel)
            Spacer(modifier = Modifier.height(24.dp))
            LongTermTicketSection()
            Spacer(modifier = Modifier.height(24.dp))
            TicketOptionsSection(navController, buyTicketViewModel)
            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom navigation
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Mua vé",
                    color = Color(0xFF1A237E), // Changed to Deep Indigo
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color(0xFF1A237E) // Changed to Deep Indigo
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Character icon
            Text(
                text = "🦁",
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column {
                Text(
                    text = "Chào mừng!",
                    color = Color(0xFF1A237E), // Changed to Deep Indigo
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Bắt đầu các trải nghiệm mới cùng Metro nhé!",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun HotSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = "🔥", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Nổi bật",
            color = Color(0xFF1A237E), // Changed to Deep Indigo
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "🔥", fontSize = 20.sp)
    }
}

@Composable
fun TicketOptionsSection(
    navController: NavHostController,
    viewModel: BuyTicketViewModel // Keep this as BuyTicketViewModel
) {
    val ticketOptions by viewModel.ticketTypes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        if (ticketOptions.isEmpty() && !isLoading && errorMessage == null) {
            viewModel.fetchTicketTypes()
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4CAF50)) // Changed to Primary Green
            }
        } else if (errorMessage != null) {
            Text(
                text = "Lỗi tải dữ liệu: $errorMessage",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        } else if (ticketOptions.isEmpty()) {
            Text(
                text = "Không có loại vé nào khả dụng.",
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            ticketOptions.forEach { ticket ->
                TicketCard(ticket = ticket, navController = navController)
            }
        }
    }
}

@Composable
fun TicketCard(
    ticket: TicketType,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable() {
                if (ticket.name == "Single") {
                    navController.navigate(Screen.StationSelection.route)
                } else {
                    navController.navigate(Screen.BuyTicketDetail.createRoute(ticket.id))
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ConfirmationNumber,
                contentDescription = ticket.description,
                tint = Color(0xFF4CAF50), // Changed to Primary Green
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = ticket.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "${ticket.price} đ",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun StudentSection(navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = "Ưu đãi",
            color = Color(0xFF1A237E), // Changed to Deep Indigo
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Học sinh",
            color = Color(0xFF1A237E), // Changed to Deep Indigo
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "🎒", fontSize = 20.sp, modifier = Modifier.padding(start = 4.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Sinh viên",
            color = Color(0xFF1A237E), // Changed to Deep Indigo
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "🎓", fontSize = 20.sp, modifier = Modifier.padding(start = 4.dp))
    }

    Spacer(modifier = Modifier.height(12.dp))

    TicketCard(
        ticket = TicketType(
            id = 0, // Placeholder ID for a hardcoded ticket
            name = "Student Monthly",
            description = "Vé tháng HSSV",
            price = 150000,
            validityDuration = "ONE_MONTH",
            isActive = true,
            createdAt = "",
            updatedAt = ""
        ),
        navController = navController
    )
}

@Composable()
fun LongTermTicketSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = "🔥", fontSize = 20.sp)

        Text(
            text = " Đừng quên mua vé dài hạn",
            color = Color(0xFF1A237E), // Changed to Deep Indigo
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "🚆",
            fontSize = 20.sp
        )
    }

}

@Composable
fun RoutesSection(
    viewModel: FareMatrixViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.fareMatrices.isEmpty() && !uiState.isLoading && uiState.errorMessage == null) {
            viewModel.fetchFareMatrices() // Trigger data fetch
        }
    }
    Column {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4CAF50)) // Changed to Primary Green
            }
        } else if (uiState.errorMessage != null) {
            Text(
                text = "Lỗi tải tuyến đường: ${uiState.errorMessage}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        } else if (uiState.fareMatrices.isEmpty()) {
            Text(
                text = "Không có tuyến đường nào khả dụng.",
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            uiState.fareMatrices.forEach { fareMatrix ->
                RouteCard(fareMatrix = fareMatrix)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun RouteCard(fareMatrix: FareMatrix) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle route selection */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Tuyến: ${fareMatrix.name}",
                    color = Color(0xFF1A237E), // Changed to Deep Indigo
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Giá: ${fareMatrix.price} đ",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }

            Text(
                text = "Xem chi tiết",
                color = Color(0xFF4CAF50), // Changed to Primary Green
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Handle details */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BuyTicketScreenPreview() {
    val navController = rememberNavController()
    BuyTicketScreen(navController = navController)
}
