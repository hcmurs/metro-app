/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import org.com.hcmurs.ui.components.common.CommonTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTopBar(navController: NavController, onAddEvent: () -> Unit) {
    CommonTopBar(
        navController = navController,
        title = "Sự kiện",
        actions = {
            TextButton(onClick = onAddEvent) {
                Text("Nhập mã", color = Color.White)
            }
        },
    )
}
