package org.com.hcmurs.ui.screens.stationselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSelectionScreen(
    navController: NavController,
    stationViewModel: StationSelectionViewModel = hiltViewModel()
) {
    val uiState by stationViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select Route") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (uiState.selectedRoute != null) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.StationSelection.route)
                    },
                    containerColor = PrimaryGreen
                ) {
                    Icon(Icons.Default.ArrowForward, "Next", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionTitle(text = "Choose your metro route")
            Spacer(modifier = Modifier.height(8.dp))

            when {
                uiState.isLoadingRoutes -> CircularProgressIndicator(color = PrimaryGreen)
                uiState.errorMessage != null && uiState.routes.isEmpty() -> {
                    Text("Error: ${uiState.errorMessage}", color = Color.Red)
                }
                uiState.routes.isNotEmpty() -> {
                    RouteSelector(
                        routes = uiState.routes,
                        selectedRoute = uiState.selectedRoute,
                        onRouteSelected = { stationViewModel.onRouteSelected(it) }
                    )

                    // Hiển thị thông tin route đã chọn
                    val selectedRoute = uiState.selectedRoute
                    if (selectedRoute != null) {
                        Spacer(modifier = Modifier.height(24.dp))
                        SelectedRouteInfo(selectedRoute = selectedRoute)
                    }
                }
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
        color = PrimaryGreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteSelector(
    routes: List<RouteResponse>,
    selectedRoute: RouteResponse?,
    onRouteSelected: (RouteResponse) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(
            items = routes,
            key = { route -> route.routeId }
        ) { route ->
            FilterChip(
                selected = route.routeId == selectedRoute?.routeId,
                onClick = { onRouteSelected(route) },
                label = { Text(route.routeName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGreen,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun SelectedRouteInfo(selectedRoute: RouteResponse) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Selected Route:",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = selectedRoute.routeName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Route Code: ${selectedRoute.routeCode}",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = "Distance: ${selectedRoute.distanceInKm} km",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}