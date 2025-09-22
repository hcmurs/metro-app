/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.metro.account

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider as HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.com.hcmurs.R
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.screens.login.LoginViewModel
import org.com.hcmurs.ui.theme.DarkGreen
import org.com.hcmurs.ui.theme.PaleYellow
import org.com.hcmurs.ui.theme.PrimaryGreen

data class MenuItem(
    val icon: ImageVector,
    val title: String,
    val hasArrow: Boolean = true,
    val isDestructive: Boolean = false,
    val onClickAction: (() -> Unit)? = null,
)

@Composable
fun MenuItemRow(
    item: MenuItem,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon Background
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = if (item.isDestructive) {
                Color(0xFFFFEBEE)
            } else {
                Color(0xFFE3F2FD)
            },
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (item.isDestructive) {
                        Color(0xFFE53935)
                    } else {
                        Color(0xFF2196F3)
                    },
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = item.title,
            color = if (item.isDestructive) {
                Color(0xFFE53935)
            } else {
                Color(0xFF424242)
            },
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )

        // Arrow
        if (item.hasArrow) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
fun AccountScreen(
    navController: NavController,
    onMenuItemClick: (MenuItem) -> Unit = {},
    viewModel: LoginViewModel,
) {
    // Lắng nghe userProfile từ ViewModel
    val userProfile by viewModel.userProfile.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.refreshUserProfile()
    }
    val userName = userProfile?.name ?: stringResource(R.string.not_updated)
    val userEmail = userProfile?.email ?: stringResource(R.string.not_updated)

    val menuItem = listOf(
        MenuItem(
            icon = Icons.Default.Person,
            title = stringResource(R.string.full_name) + ": $userName", // Cập nhật họ tên
            hasArrow = false,
        ),
        MenuItem(
            icon = Icons.Default.Email,
            title = "Email: $userEmail", // Cập nhật email
            hasArrow = false,
        ),
        if (userProfile?.isStudent == false) {
            MenuItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.account_verification),
                hasArrow = true,
                onClickAction = { navController.navigate(Screen.CCCD.route) },
            )
        } else {
            MenuItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.student_account),
                hasArrow = false,
            )
        },

//        MenuItem(
//            icon = Icons.Default.ShoppingCart,
//            title = "Quản lý phương thức thanh toán",
//            hasArrow = true
//        ),
//        MenuItem(
//            icon = Icons.Default.Clear,
//            title = "Xóa tài khoản",
//            hasArrow = false,
//            isDestructive = true
//        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryGreen,
                        PaleYellow,
                        DarkGreen,
                    ),
                ),
            ),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // status bar spacer
            Spacer(modifier = Modifier.height(24.dp))
            // header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    navController.navigate(Screen.Home.route)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                    )
                }
                Text(
                    text = stringResource(R.string.account_information),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Profile Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFE0E0E0), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF9E9E9E),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = userName, // Cập nhật tên hiển thị
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            // Content Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    // Menu Items
                    menuItem.forEach { item ->
                        MenuItemRow(
                            item = item,
                            onClick = { item.onClickAction?.invoke() },
                        )
                    }

                    // Divider
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE0E0E0),
                    )

                    // Logout Button
                    MenuItemRow(
                        item = MenuItem(
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            title = stringResource(R.string.logout),
                            hasArrow = false,
                            isDestructive = false,
                        ),
                        onClick = { viewModel.logout() }, // Gọi hàm logout từ ViewModel
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}
//
// @Preview(showBackground = true)
// @Composable
// fun AccountInfoScreenPreview() {
//    MaterialTheme {
//        AccountScreen(
//            navController = rememberNavController(),
//            viewModel = null,
//        )
//    }
// }
