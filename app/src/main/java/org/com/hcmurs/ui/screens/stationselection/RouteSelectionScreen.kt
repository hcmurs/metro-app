/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.stationselection

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.com.hcmurs.RouteResponse
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.theme.DarkGreen
import org.com.hcmurs.ui.theme.LightGreenBackground
import org.com.hcmurs.ui.theme.PrimaryGreen
import org.com.hcmurs.ui.theme.TextPrimaryColor
import org.com.hcmurs.ui.theme.TextSecondaryColor

// --- TOP BAR ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSelectionTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Chọn tuyến",
                color = PrimaryGreen,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Trở về",
                    tint = PrimaryGreen,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
    )
}

// --- WELCOME CARD ---
@Composable
fun RouteWelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Route,
                contentDescription = "Route",
                tint = Color.White,
                modifier = Modifier.size(40.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Chọn tuyến đường!",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Chọn tuyến Metro phù hợp với bạn",
                    fontSize = 14.sp,
                    color = Color(0xB3FFFFFF),
                )
            }
        }
    }
}

// --- SECTION HEADER ---
@Composable
fun RouteSectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = DarkGreen,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = TextPrimaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

// --- ROUTE CARD ---
@Composable
fun RouteSelectionCard(
    route: RouteResponse,
    isSelected: Boolean,
    onRouteSelected: (RouteResponse) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRouteSelected(route) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LightGreenBackground else Color.White,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) PrimaryGreen else Color(0xFFE0E0E0),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) PrimaryGreen else LightGreenBackground,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Route,
                        contentDescription = route.routeName,
                        tint = if (isSelected) Color.White else PrimaryGreen,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = route.routeName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryColor,
                    )
                    Text(
                        text = "Mã: ${route.routeCode}",
                        fontSize = 14.sp,
                        color = TextSecondaryColor,
                    )
                    Text(
                        text = "Khoảng cách: ${route.distanceInKm} km",
                        fontSize = 14.sp,
                        color = TextSecondaryColor,
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Chọn tuyến",
                tint = if (isSelected) PrimaryGreen else TextSecondaryColor.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

// --- ROUTES SECTION ---
@Composable
fun RoutesSelectionSection(
    routes: List<RouteResponse>,
    selectedRoute: RouteResponse?,
    onRouteSelected: (RouteResponse) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    } else if (errorMessage != null) {
        Text(
            text = "Lỗi tải tuyến đường: $errorMessage",
            color = Color.Red,
            modifier = Modifier.padding(16.dp),
        )
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            RouteSectionHeader(
                title = "Chọn tuyến Metro",
                icon = Icons.Default.Route,
            )

            routes.forEach { route ->
                RouteSelectionCard(
                    route = route,
                    isSelected = route.routeId == selectedRoute?.routeId,
                    onRouteSelected = onRouteSelected,
                )
            }
        }
    }
}

// --- MAIN SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSelectionScreen(
    navController: NavController,
    stationViewModel: StationSelectionViewModel = hiltViewModel(),
) {
    val uiState by stationViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            RouteSelectionTopBar(onBackClick = { navController.navigate(Screen.BuyTicket.route) })
        },
        containerColor = Color.White,
        floatingActionButton = {
            if (uiState.selectedRoute != null) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.StationSelection.route)
                    },
                    containerColor = PrimaryGreen,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Tiếp tục",
                        tint = Color.White,
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, LightGreenBackground),
                        startY = 0f,
                        endY = 1500f,
                    ),
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            RouteWelcomeCard()
            Spacer(modifier = Modifier.height(24.dp))

            RoutesSelectionSection(
                routes = uiState.routes,
                selectedRoute = uiState.selectedRoute,
                onRouteSelected = { stationViewModel.onRouteSelected(it) },
                isLoading = uiState.isLoadingRoutes,
                errorMessage = if (uiState.routes.isEmpty()) uiState.errorMessage else null,
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
