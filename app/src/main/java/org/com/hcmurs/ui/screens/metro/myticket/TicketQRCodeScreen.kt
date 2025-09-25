/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.com.hcmurs.R
private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
private val LightGreenBackground = Color(0xFFF1F8E9)
private val TextSecondaryColor = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketQRCodeScreen(
    navController: NavController,
    ticketCode: String,
    viewModel: TicketQRCodeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Force recomposition when returning from language change
    // This ensures strings are re-evaluated with the current locale
    LaunchedEffect(context) {
        // This will trigger when context changes (like after language change)
    }

    // Create string resources that will be re-evaluated on recomposition
    val ticketDigitalTitle = stringResource(R.string.ticket_digital)
    val scanQrGuide = stringResource(R.string.scan_qr_code_guide)
    val qrCodeNotLoaded = stringResource(R.string.qr_code_not_loaded)

    LaunchedEffect(key1 = ticketCode) {
        viewModel.fetchTicketQRCode(ticketCode)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(ticketDigitalTitle, color = DarkGreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
            )
        },
    ) { padding ->
        // Make the entire content scrollable
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiState.isLoading) {
                // Loading state with proper spacing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            } else if (uiState.errorMessage != null) {
                // Error state with proper spacing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Lỗi: ${uiState.errorMessage}",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                    )
                }
            } else if (uiState.qrCodeBitmap != null) {
                // Success state with responsive layout
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp), // Reduced padding
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Header text with reduced spacing
                    Text(
                        "",
                        fontSize = 20.sp, // Slightly smaller
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        scanQrGuide,
                        fontSize = 15.sp, // Slightly smaller
                        color = TextSecondaryColor,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(20.dp)) // Reduced spacing

                    // QR Code with adaptive size
                    Card(
                        modifier = Modifier.size(280.dp), // Smaller for compact screens
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                    ) {
                        Image(
                            bitmap = uiState.qrCodeBitmap!!.asImageBitmap(),
                            contentDescription = "QR Code for ticket ${uiState.ticketCode}",
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Ticket code display
                    uiState.ticketCode?.let {
                        Text(
                            it,
                            fontSize = 16.sp, // Slightly smaller
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.5.sp, // Slightly reduced
                            color = DarkGreen,
                        )
                    }

                    Spacer(Modifier.height(20.dp)) // Reduced spacing

                    // Countdown and reset section
                    CountdownAndReset(
                        seconds = uiState.countdownSeconds,
                        onResetClick = { viewModel.resetQRCode() },
                    )

                    // Bottom padding to ensure content is never cut off
                    Spacer(Modifier.height(24.dp))
                }
            } else {
                // Default state with proper spacing
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        qrCodeNotLoaded,
                        color = TextSecondaryColor,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun CountdownAndReset(seconds: Int, onResetClick: () -> Unit) {
    val codeRefresh = stringResource(R.string.code_refresh)
    val refreshCode = stringResource(R.string.refresh_code)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            codeRefresh,
            fontSize = 13.sp, // Slightly smaller
            color = TextSecondaryColor,
        )
        Text(
            String.format("0:%02d", seconds),
            fontSize = 18.sp, // Slightly smaller
            fontWeight = FontWeight.Bold,
            color = if (seconds <= 10) Color.Red else DarkGreen,
        )
        OutlinedButton(
            onClick = onResetClick,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, PrimaryGreen),
            modifier = Modifier.height(44.dp), // Slightly more compact
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Reset QR Code",
                tint = PrimaryGreen,
                modifier = Modifier.size(18.dp), // Slightly smaller icon
            )
            Spacer(Modifier.width(8.dp))
            Text(refreshCode, color = PrimaryGreen, fontSize = 14.sp) // Slightly smaller text
        }
    }
}
