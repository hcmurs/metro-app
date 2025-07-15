package org.com.hcmurs.ui.screens.metro.feedback

import android.R
import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Schedule
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
import org.com.hcmurs.Screen
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import android.net.Uri

import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.graphics.asImageBitmap

import android.util.Base64
import androidx.compose.foundation.Image

private val PrimaryGreen = Color(0xFF4CAF50)
private val SecondaryGreen = Color(0xFF66BB6A)
private val LightGreen = Color(0xFFE8F5E8)
private val DarkGreen = Color(0xFF2E7D32)
private val AccentGreen = Color(0xFF81C784)
private val BackgroundGray = Color(0xFFF5F7FA)
private val CardBackground = Color(0xFFFFFFFF)
private val BorderColor = Color(0xFFE1E5E9)
private val TextSecondary = Color(0xFF6B7280)
private val TextPrimary = Color(0xFF1F2937)
private val SuccessGreen = Color(0xFF10B981)
private val WarningOrange = Color(0xFFF59E0B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    navController: NavController,
    viewModel: FeedbackViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect (key1 = Unit) {
        viewModel.fetchMyFeedbacks()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Phản ánh/Góp ý",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToHome(navController) }) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Brush.horizontalGradient(
                        colors = listOf(PrimaryGreen, SecondaryGreen)
                    ).let { PrimaryGreen } // Fallback cho gradient
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.CreateFeedback.route)
                },
                containerColor = PrimaryGreen,
                contentColor = Color.White,
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(20.dp))
                    .size(64.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Gửi phản ánh mới",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = CardBackground
                        ),
                        modifier = Modifier
                            .padding(32.dp)
                            .shadow(8.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(40.dp)
                        ) {
                            CircularProgressIndicator(
                                color = PrimaryGreen,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "Đang tải dữ liệu...",
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            } else if (uiState.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFEF2F2)
                        ),
                        modifier = Modifier
                            .padding(24.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .border(
                                1.dp,
                                Color(0xFFFECACA),
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Feedback,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Đã xảy ra lỗi",
                                color = Color(0xFFDC2626),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "${uiState.errorMessage}",
                                color = Color(0xFFDC2626),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            } else if (uiState.myFeedbacks.isEmpty()) {
                EmptyFeedbackContent("Bạn chưa có phản ánh/góp ý nào")
            } else {
                LazyColumn (
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(20.dp)
    ) {
        var showImageDialog by remember { mutableStateOf(false) }
        val context= LocalContext.current

        if (showImageDialog) {
            AlertDialog (
                onDismissRequest = { showImageDialog = false },
                confirmButton = {
                    Text(
                        "Đóng",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { showImageDialog = false },
                        color = PrimaryGreen
                    )
                },
                text = {
                    val imageBytes = Base64.decode(
                        feedback.image!!.substringAfter("base64,"),
                        Base64.DEFAULT
                    )
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Ảnh phản ánh",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        }

        Column (
            modifier = Modifier.padding(24.dp)
        ) {
            // Header với gradient background
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = LightGreen
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    PrimaryGreen,
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Feedback,
                                contentDescription = "Feedback",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            feedback.category,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DarkGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content với improved styling
            Text(
                feedback.content,
                color = TextSecondary,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal
            )

            if (!feedback.image.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            showImageDialog = true
                        }
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Xem ảnh",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Xem ảnh đính kèm",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }



            // Reply section
            if (!feedback.reply.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = LightGreen
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color(0xFFA5D6A7),
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        SuccessGreen,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Reply,
                                    contentDescription = "Reply",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Phản hồi từ quản trị viên",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = DarkGreen
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            feedback.reply,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer với improved design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF9FAFB)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Time",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            feedback.createdAt,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFeedbackContent(message: String, showSearch: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 72.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            if (showSearch) {
                SearchBarWithButtons()
                Spacer(modifier = Modifier.height(32.dp))
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                modifier = Modifier.shadow(12.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(40.dp)
                ) {
                    // Gradient circle background
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        LightGreen,
                                        Color(0xFFBBDEFB)
                                    )
                                ),
                                RoundedCornerShape(70.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Feedback,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(72.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = message,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Hãy gửi phản ánh đầu tiên của bạn",
                        fontSize = 15.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBarWithButtons() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Tìm kiếm phản ánh...", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = PrimaryGreen
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    unfocusedBorderColor = BorderColor,
                    cursorColor = PrimaryGreen,
                    focusedLabelColor = PrimaryGreen
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /* TODO: Gửi phản ánh */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gửi phản ánh", fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = { /* TODO: Lọc phản ánh */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryGreen
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filter",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lọc phản ánh", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Cá nhân", "Tất cả")
    val icons = listOf(Icons.Default.Person, Icons.Default.ViewList)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, label ->
                Card(
                    modifier = Modifier
                        .clickable { onTabSelected(index) }
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedIndex == index)
                            Color.White.copy(alpha = 0.25f)
                        else Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = label,
                            tint = if (selectedIndex == index) Color.White else Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            color = if (selectedIndex == index) Color.White else Color.White.copy(alpha = 0.7f),
                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
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