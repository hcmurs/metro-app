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
import org.com.hcmurs.Screen
import org.com.hcmurs.Station
import org.com.hcmurs.ui.components.card.station.StationCard
import org.com.hcmurs.ui.components.switchentryexit.SwitchEntryExit
import org.com.hcmurs.ui.screens.metro.buyticket.FareMatrixViewModel
import org.com.hcmurs.ui.theme.GreenPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectionScreen(
    navController: NavController,
    stationViewModel: StationSelectionViewModel = hiltViewModel(),
    fareMatrixViewModel: FareMatrixViewModel
) {
    val uiState by stationViewModel.uiState.collectAsState()

    // THAY ĐỔI: Sử dụng 2 state để lưu ga Entry và Exit
    var selectedEntryStation by remember { mutableStateOf<Station?>(null) }
    var selectedExitStation by remember { mutableStateOf<Station?>(null) }
    val fareMatrixUiState by fareMatrixViewModel.uiState.collectAsState()

    var selectedAction by remember { mutableStateOf("Entry") }

    var isNavigationTriggered by remember { mutableStateOf(false) }

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

    // MỚI: Xử lý khi có lỗi tính giá vé
    LaunchedEffect(fareMatrixUiState.errorMessage) {
        Log.d(TAG, "Error message changed. Value: ${fareMatrixUiState.errorMessage}. Triggered: $isNavigationTriggered")
        if(isNavigationTriggered && fareMatrixUiState.errorMessage != null) {
            // TODO: Hiển thị Snackbar hoặc Toast thông báo lỗi
            isNavigationTriggered = false // Reset trigger
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select Entry & Exit Stations") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = GreenPrimary,
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
                    containerColor = GreenPrimary
                ) {
                    // Thêm logic hiển thị loading
                    if (fareMatrixUiState.isLoading && isNavigationTriggered) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Get Fare",
                            tint = Color.White
                        )
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
            Text(
                text = if (selectedAction == "Entry") "Please select your ENTRY station" else "Please select your EXIT station",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            SwitchEntryExit(
                selectedAction = selectedAction,
                onActionSelected = { newAction -> selectedAction = newAction }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // MỚI: Component hiển thị thông tin các ga đã chọn
            SelectedStationsSummary(entryStation = selectedEntryStation, exitStation = selectedExitStation)


            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
                uiState.errorMessage != null -> {
                    Text(text = "Error: ${uiState.errorMessage}", color = Color.Red)
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Cho phép grid co giãn
                            .padding(top = 16.dp),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.stations) { station ->
                            // THAY ĐỔI: Logic chọn và vô hiệu hóa card
                            val isSelected = when (selectedAction) {
                                "Entry" -> station == selectedEntryStation
                                "Exit" -> station == selectedExitStation
                                else -> false
                            }
                            // Ga Exit không được trùng với ga Entry
                            val isEnabled = !(selectedAction == "Exit" && station == selectedEntryStation)

                            StationCard(
                                station = station,
                                isSelected = isSelected,
                                isEnabled = isEnabled,
                                onClick = {
                                    if (isEnabled) {
                                        if (selectedAction == "Entry") {
                                            selectedEntryStation = station
                                            // Tự động chuyển sang chọn Exit nếu chưa có
                                            if (selectedExitStation == null) {
                                                selectedAction = "Exit"
                                            }
                                        } else { // "Exit"
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
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text("ENTRY", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = entryStation?.name ?: "Not Selected",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (entryStation != null) GreenPrimary else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text("EXIT", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = exitStation?.name ?: "Not Selected",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (exitStation != null) LightOrange else Color.Gray, // Giả sử LightOrange đã được định nghĩa
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
