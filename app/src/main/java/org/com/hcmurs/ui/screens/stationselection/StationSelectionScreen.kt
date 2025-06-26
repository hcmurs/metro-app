package org.com.hcmurs.ui.screens.stationselection


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.components.switchentryexit.SwitchEntryExit
import org.com.hcmurs.ui.theme.GreenPrimary
import org.osmdroid.util.GeoPoint

data class MetroStation(
    val id: Int,
    val name: String,
    val location: GeoPoint,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectionScreen(navController: NavController) {
    // Convert your GeoPoint list to MetroStation objects
    val stationList = remember {
        listOf(
            MetroStation(1, "Ben Thanh", GeoPoint(10.770696325149563, 106.69754740398896)),
            MetroStation(
                2,
                "Ga Nha Hat Thanh Pho",
                GeoPoint(10.774692602241206, 106.7022005846861)
            ),
            MetroStation(3, "Ba Son", GeoPoint(10.781671962644449, 106.70840954773367)),
            MetroStation(
                4,
                "Cong Vien Van Thanh",
                GeoPoint(10.796761528709158, 106.71677316492446)
            ),
            MetroStation(5, "Tan Cang", GeoPoint(10.79893614957232, 106.72410295882501)),
            MetroStation(6, "Thao Dien", GeoPoint(10.800635526123163, 106.73447068211519)),
            MetroStation(7, "An Phu", GeoPoint(10.802432989593624, 106.74275024588567)),
            MetroStation(8, "Rach Chiec", GeoPoint(10.808621477482305, 106.7557946347932)),
            MetroStation(9, "Ga Phuoc Long", GeoPoint(10.82165609486406, 106.75933605616969)),
            MetroStation(10, "Ga Binh Thai", GeoPoint(10.832473464905071, 106.76444401173823)),
            MetroStation(11, "Ga Thu Duc", GeoPoint(10.846639523705255, 106.7720542821162))
        )
    }

    var selectedStation by remember { mutableStateOf<MetroStation?>(null) }
    var selectedAction by remember { mutableStateOf<String>("Entry") } // Default to Entry
    var showActionDropdown by remember { mutableStateOf(false) }

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
                            Screen.ScanQrCode.createRoute(
                                selectedStation!!.id,
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stationList) { station ->
                    StationCard(
                        station = station,
                        isSelected = station == selectedStation,
                        onClick = { selectedStation = station }
                    )
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
                                navController.navigate("scanQR/${selectedStation!!.id}/${selectedStation!!.name}/${selectedAction}")
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
    station: MetroStation,
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Station ${station.id}",
                    fontSize = 12.sp,
                    color = if (isSelected) GreenPrimary else Color.Gray
                )
                Text(
                    text = station.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) GreenPrimary else Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                if (isSelected) {
                    Text(
                        text = "✓ Selected",
                        fontSize = 12.sp,
                        color = GreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StationSelectionScreenPreview() {
    val navController = rememberNavController()
    StationSelectionScreen(navController)
}