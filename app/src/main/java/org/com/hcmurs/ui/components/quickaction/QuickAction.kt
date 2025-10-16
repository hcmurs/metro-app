/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.quickaction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import org.com.hcmurs.R
import org.com.hcmurs.core.constant.ScreenTitle
import org.com.hcmurs.core.constant.UserRole
import org.com.hcmurs.ui.theme.LightOrange
import org.com.hcmurs.ui.theme.PrimaryGreen
import org.com.hcmurs.utils.getNavigationRoute
import org.com.hcmurs.utils.screenTitleIconMap

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickActionsSection(
    navController: NavHostController,
    userRole: UserRole = UserRole.GUEST,
    onGridItemClick: (String) -> Unit,
) {
    // Filter the list based on user role
    val allScreenTitles = ScreenTitle.entries

    val staffOnlyScreens = listOf(
        ScreenTitle.SCAN_QR_CODE,
    )

    val list = if (userRole == UserRole.STAFF) {
        allScreenTitles
    } else {
        allScreenTitles.filter { it !in staffOnlyScreens }
    }

    val pages = list.chunked(8)
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { pages.size },
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // Increased height to accommodate logo
            .padding(horizontal = 12.dp),
    ) {
        // Main Card with enhanced styling
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .align(Alignment.TopCenter)
                .offset(y = 25.dp), // Position below logo
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                ) { page ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .padding(top = 20.dp),
                    ) {
                        items(pages[page].size) { index ->
                            val item = pages[page][index]
                            val iconRes = screenTitleIconMap[item] ?: R.drawable.no_image
                            val painter = painterResource(id = iconRes)
                            val localizedTitle = stringResource(id = item.titleRes)

                            QuickActionItem(
                                title = localizedTitle,
                                icon = painter,
                                onClick = {
                                    val route = getNavigationRoute(item)
                                    try {
                                        onGridItemClick(route)
                                    } catch (e: Exception) {
                                        println("Navigation failed: $route - ${e.message}")
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }

        // Enhanced Page indicator
        if (pages.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                repeat(pages.size) { index ->
                    Box(
                        modifier = Modifier
                            .width(if (index == pagerState.currentPage) 24.dp else 8.dp)
                            .height(8.dp)
                            .background(
                                color = if (index == pagerState.currentPage) {
                                    LightOrange
                                } else {
                                    Color.Gray.copy(alpha = 0.3f)
                                },
                                shape = RoundedCornerShape(4.dp),
                            )
                            .clip(RoundedCornerShape(4.dp)),
                    )
                }
            }
        }

        // App Logo at top center - floating above the card
        CenterLogo()
    }
}

@Composable
fun QuickActionItem(
    title: String,
    icon: Painter,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .height(85.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Icon with enhanced background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = PrimaryGreen.copy(alpha = 0.12f),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    modifier = Modifier.size(22.dp),
                    tint = PrimaryGreen,
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Text with better readability
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 13.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuickActionsSectionMockPreview() {
    val navController = rememberNavController()

    val fakeScreens = List(8) { "Action ${it + 1}" }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE8F5E8))
            .padding(16.dp),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(fakeScreens.size) { index ->
                QuickActionItem(
                    title = fakeScreens[index],
                    icon = painterResource(id = R.drawable.ic_info),
                    onClick = {},
                )
            }
        }
    }
}

@Composable
fun CenterLogo() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp) // Increased height to accommodate logo
            .padding(horizontal = 12.dp),
    ) {
        Surface(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-10).dp), // Offset upward to overlap with banner
            shape = CircleShape,
            color = Color.White,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_hurc),
                    contentDescription = "Hurc logo",
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit, // Keeps transparency, doesn’t fill background
                )
            }
        }
    }
}

@Preview
@Composable
fun CenterLogoPreview() {
    CenterLogo()
}
