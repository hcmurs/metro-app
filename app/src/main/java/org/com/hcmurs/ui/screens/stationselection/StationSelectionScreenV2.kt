package org.com.hcmurs.ui.screens.stationselection


import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.LaunchedEffect
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
import org.com.hcmurs.Screen
import org.com.hcmurs.Station
import org.com.hcmurs.ui.components.card.station.StationCard
import org.com.hcmurs.ui.components.switchentryexit.SwitchEntryExit
import org.com.hcmurs.ui.screens.metro.buyticket.FareMatrixViewModel
import org.com.hcmurs.ui.theme.PrimaryGreen
import androidx.compose.foundation.lazy.items
import org.com.hcmurs.ui.components.switchentryexit.SwitchEntryExitVN
import org.com.hcmurs.ui.theme.LightOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectionScreen(
    navController: NavController,
    stationViewModel: StationSelectionViewModel = hiltViewModel(),
    fareMatrixViewModel: FareMatrixViewModel
) {
    val uiState by stationViewModel.uiState.collectAsState()

    var selectedEntryStation by remember { mutableStateOf<Station?>(null) }
    var selectedExitStation by remember { mutableStateOf<Station?>(null) }
    val fareMatrixUiState by fareMatrixViewModel.uiState.collectAsState()

    var selectedAction by remember { mutableStateOf("Ga vào") }

    var isNavigationTriggered by remember { mutableStateOf(false) }


    LaunchedEffect(uiState.selectedRoute) {
        if (uiState.selectedRoute == null) {
            navController.navigate(Screen.RouteSelection.route) {
                popUpTo("stationSelection") { inclusive = true }
            }
            return@LaunchedEffect
        }
        selectedEntryStation = null
        selectedExitStation = null
    }


    LaunchedEffect(fareMatrixUiState) {
        Log.d(TAG, "Fare calculated changed. Value: ${fareMatrixUiState.calculatedFare}. Triggered: $isNavigationTriggered")
        if (!isNavigationTriggered) {
            return@LaunchedEffect
        }


        if (fareMatrixUiState.isLoading) {
            return@LaunchedEffect
        }
        if (fareMatrixUiState.calculatedFare != null) {
            val entryStation = selectedEntryStation
            val exitStation = selectedExitStation
            if (entryStation != null && exitStation != null) {
                Log.d(TAG, "Navigating to CalculatedFareScreen...")
                navController.navigate(
                    Screen.CalculatedFare.createRoute(
                        entryStationId = entryStation.stationId,
                        exitStationId = exitStation.stationId
                    )
                )            }
        } else if (fareMatrixUiState.errorMessage != null) {
            Log.e(TAG, "Fare calculation failed: ${fareMatrixUiState.errorMessage}")
            isNavigationTriggered = false // Reset trigger
        }
        isNavigationTriggered = false

    }



    LaunchedEffect(fareMatrixUiState.errorMessage) {
        Log.d(TAG, "Error message changed. Value: ${fareMatrixUiState.errorMessage}. Triggered: $isNavigationTriggered")
        if(isNavigationTriggered && fareMatrixUiState.errorMessage != null) {
            // TODO: Hiển thị Snackbar hoặc Toast thông báo lỗi
            isNavigationTriggered = false
        }
    }

    LaunchedEffect(uiState.selectedRoute) {
        selectedEntryStation = null
        selectedExitStation = null
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chọn ga") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Trở lại", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (selectedEntryStation != null && selectedExitStation != null) {
                FloatingActionButton(
                    onClick = {
                        if (!fareMatrixUiState.isLoading) {
                            isNavigationTriggered = true
                            fareMatrixViewModel.getFareForStations(
                                selectedEntryStation!!.stationId,
                                selectedExitStation!!.stationId
                            )
                        }
                    },
                    containerColor = PrimaryGreen
                ) {
                    if (fareMatrixUiState.isLoading && isNavigationTriggered) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.ArrowForward, "Tính cước", tint = Color.White)
                    }
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

            val selectedRoute = uiState.selectedRoute
            if (selectedRoute != null) {
                SelectedRouteHeader(routeName = selectedRoute.routeName)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            SectionTitle(text = if (selectedAction == "Ga vào") " Chọn ga vào " else "Chọn ga ra ")
            Spacer(modifier = Modifier.height(8.dp))

            SwitchEntryExitVN (
                selectedAction = selectedAction,
                onActionSelected = { newAction -> selectedAction = newAction }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SelectedStationsSummary(entryStation = selectedEntryStation, exitStation = selectedExitStation)

            when {
                uiState.isLoadingStations -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                }
                uiState.errorMessage != null && uiState.stations.isEmpty() -> {
                    Text("Lỗi: ${uiState.errorMessage}", color = Color.Red, textAlign = TextAlign.Center)
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f).padding(top = 16.dp),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.stations.filterNotNull()) { station ->
                            val isSelected = when (selectedAction) {
                                "Ga vào" -> station.stationId == selectedEntryStation?.stationId
                                "Ga ra" -> station.stationId == selectedExitStation?.stationId
                                else -> false
                            }
                            val isEnabled = !(selectedAction == "Ga ra" && station.stationId == selectedEntryStation?.stationId)

                            StationCard(
                                station = station,
                                isSelected = isSelected,
                                isEnabled = isEnabled,
                                onClick = {
                                    if (isEnabled) {
                                        if (selectedAction == "Ga vào") {
                                            selectedEntryStation = station
                                            if (selectedExitStation == null) selectedAction = "Ga ra"
                                        } else {
                                            selectedExitStation = station
                                        }
                                    }
                                }
                            )
                        }
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
private fun SelectedRouteHeader(routeName: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = PrimaryGreen.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tuyến đường đã chọn",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = routeName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }
    }
}

@Composable
fun SelectedStationsSummary(entryStation: Station?, exitStation: Station?) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text("Ga vào", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = entryStation?.name ?: "Chưa chọn",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (entryStation != null) PrimaryGreen else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text("Ga ra", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = exitStation?.name ?: "Chưa chọn",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (exitStation != null) LightOrange else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

val LightOrange = Color(0xFFFFA726)

//
//@Preview(showBackground = true)
//@Composable
//fun StationSelectionScreenPreview() {
//    StationSelectionScreen(navController = rememberNavController(),
//        )
//}