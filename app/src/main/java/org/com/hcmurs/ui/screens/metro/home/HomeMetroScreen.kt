package org.com.hcmurs.ui.screens.metro.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.constant.ScreenTitle
import org.com.hcmurs.Screen
import org.com.hcmurs.utils.getNavigationRoute
import org.com.hcmurs.R
import org.com.hcmurs.ui.components.WeatherDisplay

// Fake news data
data class NewsItem(
    val id: Int,
    val title: String,
    val summary: String,
    val imageUrl: String,
    val publishTime: String,
    val category: String
)

val fakeNewsData = listOf(
    NewsItem(1, "Hệ thống Metro mới được nâng cấp", "Hệ thống vận hành được cải thiện đáng kể với công nghệ AI mới nhất", "", "2 giờ trước", "Công nghệ"),
    NewsItem(2, "Kết nối 5G trong tàu điện ngầm", "Triển khai mạng 5G tốc độ cao cho toàn bộ hệ thống tàu điện", "", "4 giờ trước", "Mạng"),
    NewsItem(3, "Bảo trì định kỳ tuyến số 1", "Thông báo lịch bảo trì và điều chỉnh giờ hoạt động", "", "6 giờ trước", "Thông báo"),
    NewsItem(4, "Ứng dụng di động mới", "Ra mắt tính năng thanh toán không tiếp xúc qua ứng dụng", "", "1 ngày trước", "Ứng dụng"),
    NewsItem(5, "Mở rộng tuyến Metro", "Kế hoạch mở rộng 3 tuyến mới trong năm 2025", "", "2 ngày trước", "Mở rộng"),
    NewsItem(6, "An toàn hành khách", "Triển khai hệ thống giám sát AI để đảm bảo an toàn", "", "3 ngày trước", "An toàn")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMetroScreen(navController: NavHostController) {
    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset > 0 }
    }

    Scaffold(
        topBar = {
            HomeTopBar(isScrolled = isScrolled)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Handle emergency call */ },
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                modifier = Modifier.shadow(8.dp, CircleShape)
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Gọi khẩn cấp")
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE8F5E8),
                            Color.White
                        )
                    )
                ),
            contentPadding = padding
        ) {
            item {
                // Hero Section with Gradient Background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4CAF50),
                                    Color(0xFF66BB6A)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        WelcomeSection()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Quản lý hệ thống Metro thông minh",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                QuickActionsSection(navController)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                ManagementSection()
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                NewsSection()
            }

            item {
                Spacer(modifier = Modifier.height(100.dp)) // Space for FAB
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(isScrolled: Boolean) {
    var selectedLanguage by remember { mutableStateOf("Vietnamese") }
    // Weather data - would come from ViewModel in real app
    val temperature = remember { mutableStateOf(27.5) }
//    val windSpeed = remember { mutableStateOf(3.2) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherDisplay(
                    temperature = temperature.value,
                    isScrolled = isScrolled
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Thông báo",
                            tint = if (isScrolled) Color(0xFF4CAF50) else Color(0xFF4CAF50)
                        )
                    }
                    LanguageDropdown(
                        selectedLanguage,
                        onLanguageChange = { selectedLanguage = it },
                        isScrolled = isScrolled
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isScrolled) Color(0xFF4CAF50) else Color.Transparent
        ),
        modifier = if (isScrolled) Modifier.shadow(4.dp) else Modifier
    )
}

@Composable
fun LanguageDropdown(selected: String, onLanguageChange: (String) -> Unit, isScrolled: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    val vietnam = "\uD83C\uDF0F Tiếng Việt"
    val english = "\uD83C\uDF0F English"

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Card(
            modifier = Modifier.clickable { expanded = true },
            colors = CardDefaults.cardColors(
                containerColor = if (isScrolled) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = if (selected == "Vietnamese") vietnam else english,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = if (isScrolled) Color.White else Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Vietnamese", "English").forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang) },
                    onClick = {
                        onLanguageChange(lang)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun WelcomeSection() {
    Column {
        Text(
            text = "Chào mừng trở lại!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Hôm nay là ${getCurrentDate()}",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

// Helper function for current date
@Composable
fun getCurrentDate(): String {
    return "Thứ 5, 29 tháng 5, 2025"
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickActionsSection(navController: NavHostController) {
    val list = ScreenTitle.values().toList()
    val pages = list.chunked(8)
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { pages.size }
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().background(Color.Transparent)
            ) { page ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize(),

                ) {
                    items(pages[page].size) { index ->
                        val item = pages[page][index]
                        QuickActionItem(
                            title = item.title,
                            onClick = {
                                val route = getNavigationRoute(item)
                                try {
                                    navController.navigate(route)
                                } catch (e: Exception) {
                                    println("Navigation failed: $route - ${e.message}")
                                }
                            }
                        )
                    }
                }
            }
        }

        // Page indicator
        if (pages.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (index == pagerState.currentPage) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.3f),
                                CircleShape
                            )
                            .padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ManagementSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Quản lý hệ thống",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ManagementTile(
                title = "Quản lý Node",
                subtitle = "Giám sát thiết bị",
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            ) {
                // TODO: Navigation
            }
            ManagementTile(
                title = "Giám sát",
                subtitle = "Theo dõi realtime",
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            ) {
                // TODO: Navigation
            }
        }
    }
}

@Composable
fun ManagementTile(
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .width(160.dp) // optional fixed width for grid layout
            .wrapContentHeight()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // Large image on top
            Image(
                painter = painterResource(id = R.drawable.hurc),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            // Text content below
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }
        }
    }
}


@Composable
fun NewsSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tin tức mới nhất",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* TODO: See all news */ }) {
                Text("Xem tất cả")
            }
        }

        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            fakeNewsData.forEach { news ->
                NewsTile(news = news)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun NewsTile(news: NewsItem) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .clickable { /* TODO: Navigate to news detail */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // News image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF66BB6A)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hurc),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = news.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = news.summary,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = news.publishTime,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeMetroScreenPreview() {
    val navController = rememberNavController()
    HomeMetroScreen(navController = navController)
}