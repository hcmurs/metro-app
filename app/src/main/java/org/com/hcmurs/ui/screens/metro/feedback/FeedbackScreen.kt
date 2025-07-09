package org.com.hcmurs.ui.screens.metro.feedback

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.repositories.apis.feedback.FeedbackDto
import org.com.hcmurs.utils.navigateToHome
import androidx.compose.foundation.lazy.items
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    navController: NavController,
    viewModel: FeedbackViewModel = hiltViewModel() // Sử dụng ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Tải danh sách feedback của người dùng khi vào màn hình
    LaunchedEffect (key1 = Unit) {
        viewModel.fetchMyFeedbacks()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Phản ánh/Góp ý", fontWeight = FontWeight.SemiBold, color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navigateToHome(navController) }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF2D1E66))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Mở màn hình tạo feedback mới */ },
                containerColor = Color.Green
            ) {
                Icon(Icons.Default.Add, contentDescription = "Gửi phản ánh mới", tint = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Lỗi: ${uiState.errorMessage}", color = Color.Red)
                }
            } else if (uiState.myFeedbacks.isEmpty()) {
                EmptyFeedbackContent("Bạn chưa có phản ánh/góp ý nào")
            } else {
                LazyColumn (
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items (uiState.myFeedbacks,
                        key = { feedback: FeedbackDto -> feedback.feedbackId })
                    { feedback ->
                        FeedbackCard(feedback = feedback)
                    }
                }
            }
        }
    }
}
@Composable
fun FeedbackCard(feedback: FeedbackDto) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column (Modifier.padding(16.dp)) {
            Text(feedback.category, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Text(feedback.content, color = Color.Gray, fontSize = 14.sp)
            if (!feedback.reply.isNullOrBlank()) {
                Divider(Modifier.padding(vertical = 8.dp))
                Text("Phản hồi từ quản trị viên:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF005BAC))
                Text(feedback.reply, fontSize = 14.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Gửi lúc: ${feedback.createdAt}",
                fontSize = 12.sp,
                color = Color.LightGray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
@Composable
fun EmptyFeedbackContent(message: String, showSearch: Boolean = false) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 72.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (showSearch) {
                SearchBarWithButtons()
                Spacer(modifier = Modifier.height(32.dp))
            }
            Image(
                painter = painterResource(id = R.drawable.ic_menu_help), // Replace with your image
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, fontSize = 16.sp)
        }

        FloatingActionButton(
            onClick = { /* TODO: Handle new feedback */ },
            containerColor = Color.Green,
            contentColor = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd), // <-- hợp lệ khi nằm trong Box
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun SearchBarWithButtons() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Tìm kiếm") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { /* TODO: Gửi phản ánh */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Gửi phản ánh")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* TODO: Lọc phản ánh */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Lọc phản ánh")
            }
        }
    }
}

@Composable
fun BottomNavBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Cá nhân", "Tất cả")
    val icons = listOf(Icons.Default.Person, Icons.Default.ViewList)

    BottomAppBar(
        containerColor = Color(0xFF005BAC)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, label ->
                Column(
                    modifier = Modifier
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = icons[index],
                        contentDescription = label,
                        tint = Color.White
                    )
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = if (selectedIndex == index) Color.Yellow else Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackScreenPreview() {
    MaterialTheme {
        FeedbackScreen(navController = rememberNavController())
    }
}
