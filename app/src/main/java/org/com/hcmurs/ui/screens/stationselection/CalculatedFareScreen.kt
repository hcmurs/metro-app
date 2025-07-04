package org.com.hcmurs.ui.screens.stationselection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.com.hcmurs.FareMatrix
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.screens.metro.buyticket.FareMatrixViewModel
import org.com.hcmurs.ui.theme.DarkGreen

private val PrimaryGreen = Color(0xFF4CAF50)
private val DarkGreen = Color(0xFF388E3C)
private val LightGreenBackground = Color(0xFFE8F5E9)
private val TextPrimaryColor = Color(0xFF212121)
private val TextSecondaryColor = Color(0xFF757575)
private val WarningColor = Color(0xFFD32F2F)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatedFareScreen(
    navController: NavHostController,
    entryStationId: Int,
    exitStationId: Int,
    viewModel: FareMatrixViewModel,
    stationViewModel: StationSelectionViewModel = hiltViewModel()

) {
    val uiState by viewModel.uiState.collectAsState()
    val fare = uiState.calculatedFare
    val stationUiState by stationViewModel.uiState.collectAsState()

    val entryStation = stationUiState.stations.find { it.stationId == entryStationId }
    val exitStation = stationUiState.stations.find { it.stationId == exitStationId }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Xác nhận hành trình", color = DarkGreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkGreen)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        val currentFareResponse = uiState.calculatedFare
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, LightGreenBackground),
                        startY = 0f,
                        endY = 1500f
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = PrimaryGreen)
            }else if (currentFareResponse != null && entryStation != null && exitStation != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(16.dp))
                    FareDetailCard(
                        entryStationName = entryStation.name,
                        exitStationName = exitStation.name,
                        fare = currentFareResponse.data!!
                    )
                }
            } else {
                Text(
                    text = uiState.errorMessage ?: "Không thể tính giá vé. Vui lòng thử lại.",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            // Các nút hành động
            Column {
                Button (
                    onClick = {
                        if (entryStation != null && exitStation != null) {
                            navController.navigate(
                                Screen.OrderFareInfo.createRoute(
                                        entryStationId = entryStation.stationId,
                                    exitStationId = exitStation.stationId
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    enabled = fare != null
                ) {
                    Text("Xác nhận mua vé", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.5f))
                ) {
                    Text("Chọn lại ga", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = PrimaryGreen)
                }
            }
        }
    }
}

@Composable
fun FareDetailCard(entryStationName: String, exitStationName: String, fare: FareMatrix) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "HÀNH TRÌNH CỦA BẠN",
                fontSize = 14.sp,
                color = TextSecondaryColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))

            Divider(color = LightGreenBackground.copy(alpha=0.8f), thickness = 1.dp)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoRow(title = "Hạn sử dụng:", value = "Theo quy định")
                InfoRow(title = "Lưu ý:", value = "Tự động kích hoạt sau 30 ngày kể từ ngày mua.", valueColor = WarningColor)
                InfoRow(title = "Mô tả:", value = "Vé cho phép di chuyển một lượt giữa ${entryStationName} và ${exitStationName}.")
            }
            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = LightGreenBackground.copy(alpha=0.8f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))


            // Hiển thị ga vào - ga ra
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                StationDisplay(name = entryStationName, isEntry = true)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "to",
                    tint = TextSecondaryColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                StationDisplay(name = exitStationName, isEntry = false)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = LightGreenBackground)
            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị giá
            Text("TỔNG CỘNG", fontSize = 14.sp, color = TextSecondaryColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${fare.price} đ",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkGreen
            )
        }
    }
}

@Composable
fun StationDisplay(name: String, isEntry: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(if (isEntry) PrimaryGreen else Color(0xFFFFA726)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = "Station",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
            lineHeight = 20.sp,
            color = TextPrimaryColor
        )
    }
}


@Composable
private fun InfoRow(title: String, value: String, valueColor: Color = TextPrimaryColor) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = TextSecondaryColor,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
//@Preview(showBackground = true)
//@Composable
//fun CalculatedFareScreenPreview() {
//    CalculatedFareScreen(
//        navController = rememberNavController(),
//        entryStationId = 1,
//        exitStationId = 2
//    )
//}