package org.com.hcmurs.ui.screens.metro.myticket

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
private val LightGreenBackground = Color(0xFFF1F8E9)
private val TextSecondaryColor = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketQRCodeScreen(
    navController: NavController,
    ticketCode: String,
    viewModel: TicketQRCodeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = ticketCode) {
        viewModel.fetchTicketQRCode(ticketCode)
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Vé điện tử", color = DarkGreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton (onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(LightGreenBackground),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = PrimaryGreen)
            } else if (uiState.errorMessage != null) {
                Text("Lỗi: ${uiState.errorMessage}", color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
            } else if (uiState.qrCodeBitmap != null) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Vé lượt", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkGreen)

                    Spacer(Modifier.height(8.dp))

                    Text("Vui lòng quét mã tại cổng soát vé", fontSize = 16.sp, color = TextSecondaryColor)

                    Spacer(Modifier.height(24.dp))

                    // Hiển thị QR Code từ Bitmap
                    Card (
                        modifier = Modifier.size(250.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Image(
                            bitmap = uiState.qrCodeBitmap!!.asImageBitmap(),
                            contentDescription = "QR Code for ticket ${uiState.ticketCode}",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    uiState.ticketCode?.let {
                        Text(
                            it,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 2.sp
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    CountdownAndReset(
                        seconds = uiState.countdownSeconds,
                        onResetClick = { viewModel.resetQRCode() }
                    )
                }
            } else {
                Text("Không thể tải được mã QR.", color = TextSecondaryColor, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun CountdownAndReset(seconds: Int, onResetClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Mã sẽ được làm mới sau:",
            fontSize = 14.sp,
            color = TextSecondaryColor
        )
        Text(
            String.format("0:%02d", seconds),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (seconds <= 10) Color.Red else DarkGreen
        )
        OutlinedButton(
            onClick = onResetClick,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, PrimaryGreen)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Reset QR Code", tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Làm mới mã", color = PrimaryGreen)
        }
    }
}