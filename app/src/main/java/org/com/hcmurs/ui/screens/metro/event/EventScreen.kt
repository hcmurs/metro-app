package org.com.hcmurs.ui.screens.metro.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.com.hcmurs.model.Event
import org.com.hcmurs.ui.components.card.event.EventCard
import org.com.hcmurs.ui.components.topbar.EventTopBar
import org.com.hcmurs.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavHostController) {
    // Sample events data - empty for demonstration
//    val events = remember { emptyList<Event>() }


    val events = remember {
        listOf(
            Event("1", "Khai trương tuyến Metro", "Sự kiện khai trương tuyến metro số 1", "20/10/2023"),
            Event("2", "Giảm giá vé", "Giảm giá vé 50% trong dịp lễ", "30/10/2023"),
            Event("3", "Hội chợ tại ga Bến Thành", "Triển lãm văn hóa tại ga Bến Thành", "15/11/2023")
        )
    }


    // State for dialog
    var showDialog by remember { mutableStateOf(false) }
    var titleInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }

    // Add dialog if needed
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Sự kiện, khuyến mãi", fontSize = 20.sp) },
            containerColor = Color.White,
            text = {
                Column {
                    Text(
                        text = "Nhập mã sự kiện, khuyến mãi để nhận được các khuyến mãi hay tham gia sự kiện đang diễn ra",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        placeholder = { Text("Nhập mã") },
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = MaterialTheme.shapes.small
                            ),
//                        colors = TextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            unfocusedIndicatorColor = PrimaryGreen,
//                            focusedIndicatorColor = PrimaryGreen
//                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Handle submission
                        showDialog = false
                        titleInput = ""
                        descriptionInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen, // Background color
                        contentColor = Color.White  // Text color
                    )
                ) {
                    Text("Đồng ý")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Hủy")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            EventTopBar(
                navController = navController,
                onAddEvent = { showDialog = true }
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
                        EventCard(event)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview() {
    EventScreen(navController = rememberNavController())
}