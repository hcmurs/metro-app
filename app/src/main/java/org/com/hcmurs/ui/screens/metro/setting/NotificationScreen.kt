/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.metro.setting

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.com.hcmurs.ui.theme.DarkGreen
import org.com.hcmurs.ui.theme.LightGreen
import org.com.hcmurs.ui.theme.PrimaryGreen
import org.com.hcmurs.ui.theme.SecondaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thông báo",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Mark all as read */ }) {
                        Icon(
                            Icons.Default.DoneAll,
                            contentDescription = "Mark all read",
                            tint = Color.White,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            // Header Stats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LightGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = PrimaryGreen,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "5",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                        )
                        Text(
                            "Mới",
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .height(60.dp)
                            .width(1.dp),
                        color = PrimaryGreen.copy(alpha = 0.3f),
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Default.Archive,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = SecondaryGreen,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "12",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                        )
                        Text(
                            "Đã đọc",
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                        )
                    }
                }
            }

            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    onClick = { },
                    label = {
                        Text("Tất cả")
                    },
                    selected = true,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryGreen,
                        selectedLabelColor = Color.White,
                    ),
                )
                FilterChip(
                    onClick = { },
                    label = {
                        Text("Chưa đọc")
                    },
                    selected = false,
                )
                FilterChip(
                    onClick = { },
                    label = { Text("Quan trọng") },
                    selected = false,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Notifications List
            NotificationItem(
                title = "Xác thực CCCD thành công",
                message = "Căn cước công dân của bạn đã được xác thực thành công. Bạn có thể sử dụng các tính năng liên kết với CCCD.",
                time = "2 phút trước",
                isRead = false,
                isImportant = true,
                icon = Icons.Default.CheckCircle,
                iconColor = PrimaryGreen,
            )

            NotificationItem(
                title = "Cập nhật ứng dụng",
                message = "Phiên bản mới 1.3.20 đã có sẵn với nhiều tính năng cải tiến và sửa lỗi.",
                time = "1 giờ trước",
                isRead = false,
                isImportant = false,
                icon = Icons.Default.SystemUpdate,
                iconColor = Color(0xFF2196F3),
            )

            NotificationItem(
                title = "Gia hạn CCCD sắp hết hạn",
                message = "CCCD của bạn sẽ hết hạn trong 30 ngày. Vui lòng liên hệ cơ quan có thẩm quyền để gia hạn.",
                time = "3 giờ trước",
                isRead = false,
                isImportant = true,
                icon = Icons.Default.Warning,
                iconColor = Color(0xFFFF9800),
            )

            NotificationItem(
                title = "Bảo trì hệ thống",
                message = "Hệ thống sẽ được bảo trì từ 2:00 - 4:00 sáng ngày mai. Các tính năng có thể bị gián đoạn.",
                time = "5 giờ trước",
                isRead = true,
                isImportant = false,
                icon = Icons.Default.Build,
                iconColor = Color(0xFF607D8B),
            )

            NotificationItem(
                title = "Khuyến mãi vé tháng",
                message = "Giảm 20% cho vé tháng học sinh - sinh viên. Áp dụng từ ngày 1-15 tháng này.",
                time = "1 ngày trước",
                isRead = true,
                isImportant = false,
                icon = Icons.Default.LocalOffer,
                iconColor = Color(0xFFE91E63),
            )

            NotificationItem(
                title = "Cập nhật chính sách bảo mật",
                message = "Chúng tôi đã cập nhật chính sách bảo mật để bảo vệ thông tin cá nhân của bạn tốt hơn.",
                time = "2 ngày trước",
                isRead = true,
                isImportant = false,
                icon = Icons.Default.Security,
                iconColor = PrimaryGreen,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NotificationItem(
    title: String,
    message: String,
    time: String,
    isRead: Boolean,
    isImportant: Boolean,
    icon: ImageVector,
    iconColor: Color,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) Color.White else LightGreen.copy(alpha = 0.3f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isRead) 1.dp else 3.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // Icon with background
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = iconColor.copy(alpha = 0.1f),
                ),
                modifier = Modifier.size(40.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        title,
                        fontSize = 14.sp,
                        fontWeight = if (isRead) FontWeight.Medium else FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.weight(1f),
                    )

                    if (isImportant) {
                        Icon(
                            Icons.Default.PriorityHigh,
                            contentDescription = "Important",
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    if (!isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    PrimaryGreen,
                                    shape = RoundedCornerShape(4.dp),
                                ),
                        )
                    }
                }

                Text(
                    message,
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )

                Text(
                    time,
                    fontSize = 11.sp,
                    color = Color(0xFF999999),
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(
        navController = NavController(LocalContext.current),
    ) // Replace with actual NavController context
}
