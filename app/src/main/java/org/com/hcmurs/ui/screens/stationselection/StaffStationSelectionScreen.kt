/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.ui.screens.stationselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.com.hcmurs.RouteResponse
import org.com.hcmurs.Station
import org.com.hcmurs.ui.components.card.station.StationCard
import org.com.hcmurs.ui.screens.scanqr.ActionType
import org.com.hcmurs.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffStationSelectionScreen(
    navController: NavController,
    stationViewModel: StationSelectionViewModel = hiltViewModel(),
    actionType: ActionType,
) {
    var selectedStation by remember { mutableStateOf<Station?>(null) }
    val uiState by stationViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select Station for ${actionType.name}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White,
                ),
            )
        },
        floatingActionButton = {
            if (selectedStation != null) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("scanQR/${selectedStation!!.stationId}/${selectedStation!!.name}/${actionType.name}")
                    },
                    containerColor = PrimaryGreen,
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Continue",
                        tint = Color.White,
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            // Routes section
            SectionTitle("Select Route")

            if (uiState.isLoadingRoutes) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                ) {
                    items(uiState.routes) { route ->
                        RouteChip(
                            route = route,
                            isSelected = uiState.selectedRoute?.routeId == route.routeId,
                            onClick = { stationViewModel.onRouteSelected(route) },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stations section
            if (uiState.selectedRoute != null) {
                SectionTitle("Select Station")

                if (uiState.isLoadingStations) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                } else if (uiState.stations.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(uiState.stations) { station ->
                            StationCard(
                                station = station,
                                isSelected = station == selectedStation,
                                onClick = { selectedStation = station },
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No stations available for this route",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                    )
                }
            } else {
                Text(
                    text = "Please select a route first",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 16.sp,
                )
            }

            // Error message
            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryGreen,
        modifier = Modifier.padding(vertical = 8.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteChip(
    route: RouteResponse,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        onClick = onClick,
        label = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = route.routeName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                Text(
                    text = route.routeCode,
                    fontSize = 12.sp,
                )
            }
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryGreen,
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = PrimaryGreen,
        ),
        modifier = Modifier.padding(horizontal = 4.dp),
    )
}
