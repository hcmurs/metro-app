/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.components.button.LanguageButton
import org.com.hcmurs.ui.components.button.NotificationButton
import org.com.hcmurs.ui.components.weather.WeatherDisplay
import org.com.hcmurs.ui.components.weather.WeatherViewModel
import org.com.hcmurs.ui.screens.notification.NotificationViewModel
import org.com.hcmurs.ui.theme.PrimaryGreen

// ✅ Wrapper Composable (uses ViewModel + NavController)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isScrolled: Boolean = false,
    isAuthenticated: Boolean = false,
) {
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    HomeTopBarContent(
        modifier = modifier,
        isScrolled = isScrolled,
        unreadCount = unreadCount,
        onNotificationClick = { navController.navigate(Screen.Notification.route) },
        onChangeLanguageClick = { navController.navigate(Screen.ChangeLanguage.route) },
        weatherContent = {
            WeatherDisplay(viewModel = weatherViewModel, isScrolled = isScrolled)
        },
    )
}

// ✅ Pure composable — easy to preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBarContent(
    modifier: Modifier = Modifier,
    isScrolled: Boolean,
    unreadCount: Int,
    onNotificationClick: () -> Unit,
    onChangeLanguageClick: () -> Unit,
    weatherContent: @Composable () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                weatherContent()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    NotificationButton(
                        unreadCount = unreadCount,
                        onClick = onNotificationClick,
                    )
                    LanguageButton(
                        isScrolled = isScrolled,
                        onClick = onChangeLanguageClick,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isScrolled) {
                PrimaryGreen
            } else {
                Color.Black.copy(alpha = 0.3f)
            },
        ),
        modifier = if (isScrolled) modifier.shadow(4.dp) else modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeTopBarPreview() {
    HomeTopBarContent(
        isScrolled = false,
        unreadCount = 3,
        onNotificationClick = {},
        onChangeLanguageClick = {},
        weatherContent = {
            Text(
                text = "☀️ 33°C - Sunny",
                color = Color.White,
                fontSize = 16.sp,
            )
        },
    )
}
