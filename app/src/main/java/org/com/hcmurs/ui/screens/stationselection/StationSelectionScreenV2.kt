package org.com.hcmurs.ui.screens.stationselection


import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator // NEW: For loading state
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect // NEW: For initial data fetch
import androidx.compose.runtime.collectAsState // NEW: To collect flow state
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.components.switchentryexit.SwitchEntryExit
import org.com.hcmurs.ui.theme.GreenPrimary
import org.osmdroid.util.GeoPoint
import androidx.hilt.navigation.compose.hiltViewModel // NEW: Import hiltViewModel
import org.com.hcmurs.Station // NEW: Import Station data class


// Remove the hardcoded MetroStation data class here.
// Use the 'Station' data class from 'org.com.hcmurs.data.Station' which matches your API.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectionScreen(
    navController: NavController,
    viewModel: StationSelectionViewModel = hiltViewModel() // Inject ViewModel
) {
    val uiState by viewModel.uiState.collectAsState() // Collect UI state

    var selectedStation by remember { mutableStateOf<Station?>(null) } // Use Station from API
    var selectedAction by remember { mutableStateOf<String>("Entry") } // Default to Entry

    // Trigger data fetch when the screen is first composed
    LaunchedEffect(Unit) {
        if (uiState.stations.isEmpty() && !uiState.isLoading && uiState.errorMessage == null) {
            viewModel.fetchStations()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select Your Station") },
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
            if (selectedStation != null) {
                FloatingActionButton(
                    onClick = {
                        // Navigate to QR scan screen with selected station data
                        navController.navigate(
                            // Ensure Screen.ScanQrCode.createRoute expects stationId, name, action
                            Screen.ScanQrCode.createRoute(
                                selectedStation!!.stationId, // Use stationId from API
                                selectedStation!!.name,
                                selectedAction
                            )
                        )
                    },
                    containerColor = GreenPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Continue",
                        tint = Color.White
                    )
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
            SwitchEntryExit(
                selectedAction = selectedAction,
                onActionSelected = { selectedAction = it }
            )

            // Display loading, error, or station list based on UI state
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GreenPrimary)
                    }
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = "Error: ${uiState.errorMessage}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                uiState.stations.isEmpty() -> {
                    Text(
                        text = "No stations available.",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.stations) { station -> // Use uiState.stations
                            StationCard(
                                station = station, // Pass Station object
                                isSelected = station == selectedStation,
                                onClick = { selectedStation = station }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Selected station card
            if (selectedStation != null) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Selected Station",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = selectedStation!!.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        )
                        Button(
                            onClick = {
                                // Ensure ScanQrCode route is updated in Screen.kt to accept these parameters
                                navController.navigate(
                                    Screen.ScanQrCode.createRoute(
                                        selectedStation!!.stationId, // Use stationId from API
                                        selectedStation!!.name,
                                        selectedAction
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenPrimary
                            )
                        ) {
                            Text("Continue to Scan QR")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StationCard(
    station: Station, // Changed to Station data class
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) GreenPrimary.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, GreenPrimary) else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Station ${station.sequenceOrder}", // Use sequenceOrder
                    fontSize = 12.sp,
                    color = if (isSelected) GreenPrimary else Color.Gray
                )
                Text(
                    text = station.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) GreenPrimary else Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationSelectionScreenPreview1() {
    val navController = rememberNavController()
    StationSelectionScreen(navController)
}