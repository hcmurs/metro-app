package org.com.hcmurs.ui.screens.metro.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import org.com.hcmurs.R
import org.com.hcmurs.constant.ScreenTitle
import org.com.hcmurs.ui.components.floatingButton.FloatingButton
import org.com.hcmurs.ui.components.topbar.HomeTopBar
import org.com.hcmurs.ui.screens.guide.GuideSection
import org.com.hcmurs.ui.screens.news.NewsSection
import org.com.hcmurs.ui.theme.GreenPrimary
import org.com.hcmurs.ui.theme.LightOrange
import org.com.hcmurs.utils.getNavigationRoute
import org.com.hcmurs.utils.screenTitleIconMap

@Composable
fun HomeScreen(navController: NavHostController) {
    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset > 0 }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content with LazyColumn
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
            contentPadding = PaddingValues(
                top = 0.dp, // Remove top padding since topbar is floating
                bottom = 240.dp // Add bottom padding for floating QuickActions (200dp + 40dp margin)
            )
        ) {
            item {
                Box(modifier = Modifier.height(450.dp)) {
                    // Banner background
                    AsyncImage(
                        model = R.drawable.login_banner,
                        contentDescription = "Social link",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                            .background(Color.White),
                        contentScale = ContentScale.Crop
                    )

                    // Floating card - anchored to bottom of image
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 0.dp)
                            .offset(y = 30.dp) // Push it a bit downward if needed
                    ) {
                        QuickActionsSection(navController)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                Box(modifier = Modifier.padding(start = 16.dp)) {
                    GuideSection()
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.padding(start = 16.dp)) {
                    NewsSection()
                }

            }
        }

        // Floating TopBar
        HomeTopBar(
            isScrolled = isScrolled,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Floating Action Button
        FloatingButton(
            onClick = {
                Log.d("FloatingButton", "Clicked!")
            },
            icon = Icons.Default.Phone,
            contentDescription = "Phone Icon",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 50.dp, end = 16.dp)
        )
    }
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
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) { page ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier.fillMaxSize(),

                        ) {
                        items(pages[page].size) { index ->
                            val item = pages[page][index]
                            val iconRes = screenTitleIconMap[item] ?: R.drawable.btn_5 // fallback
                            val painter = painterResource(id = iconRes)
                            val localizedTitle =
                                stringResource(id = item.titleRes) // Use string resource

                            QuickActionItem(
                                title = localizedTitle,
                                icon = painter,
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
                                if (index == pagerState.currentPage) LightOrange else Color.Gray.copy(
                                    alpha = 0.3f
                                ),
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
fun QuickActionItem(
    title: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(90.dp) // FIXED: Set fixed height to prevent overlap
            .padding(4.dp) // FIXED: Reduced padding to fit better
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp), // FIXED: Reduced internal padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // FIXED: Better spacing distribution
        ) {
            // Icon section
            Box(
                modifier = Modifier
                    .size(32.dp) // FIXED: Slightly smaller icon to save space
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    modifier = Modifier.size(18.dp), // FIXED: Smaller icon size
                    tint = Color(0xFF4CAF50)
                )
            }

            // Text section
            Text(
                text = title,
                fontSize = 10.sp, // FIXED: Slightly smaller text
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 12.sp // FIXED: Better line height for 2-line text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeMetroScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}