package org.com.hcmurs.ui.screens.metro.ticketinformation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.model.TicketInformation
import org.com.hcmurs.ui.components.card.ticketinformation.TicketInformationCard
import org.com.hcmurs.ui.components.topbar.TicketInformationTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketInformationScreen(navController: NavHostController) {
    // Sample events data - empty for demonstration
//    val events = remember { emptyList<Event>() }


    val events = remember {
        listOf(
            TicketInformation("1", "GO!METRO với đa dạng hình thức đi tàu", " Hành trình di chuyển trong thành phố chưa bao giờ thuận tiện đến thế ", "4 tháng trước"),
            TicketInformation("2", "Bảng giá vé tuyến metro số 1 Bến Thành - Suối Tiên", "\uD83D\uDD0A Học sinh, sinh viên được giảm 50%, chỉ còn 150.000 đồng/tháng", "6 tháng trước"),
            TicketInformation("3", "Đề xuất giá vé tuyến metro Bến Thành - Suối Tiên", "Vé tàu metro Bến Thành - Suối Tiên cao nhất 24.000 đồng/lượt", "khoảng 1 năm trước")
        )
    }

    Scaffold(
        topBar = {
            TicketInformationTopBar(
                navController = navController,
            )
        },
        containerColor = Color.White,
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (events.isEmpty()) {
                // Fixed empty state with vertical arrangement
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = org.com.hcmurs.R.drawable.hurc),
                        contentDescription = "HURC Logo",
                        modifier = Modifier.size(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Không có dữ liệu",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Show event list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(events) { event ->
                        TicketInformationCard(event)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketInformationPreview() {
    TicketInformationScreen(navController = rememberNavController())
}