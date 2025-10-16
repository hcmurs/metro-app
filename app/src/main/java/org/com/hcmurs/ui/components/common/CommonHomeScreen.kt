/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.com.hcmurs.R
import org.com.hcmurs.core.constant.UserRole
import org.com.hcmurs.ui.components.floatingButton.FloatingUtility
import org.com.hcmurs.ui.components.quickaction.QuickActionsSection
import org.com.hcmurs.ui.components.topbar.HomeTopBar
import org.com.hcmurs.ui.screens.login.LoginViewModel

@Composable
fun AppHomeScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    role: UserRole = UserRole.GUEST,
    onGridItemClick: (String) -> Unit,
    contentAfterBanner: LazyListScope.() -> Unit,
) {
    val listState = rememberLazyListState()
    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE8F5E8), Color.White),
                    ),
                ),
            contentPadding = PaddingValues(top = 0.dp, bottom = 240.dp),
        ) {
            item {
                Box(modifier = Modifier.height(480.dp)) {
                    AsyncImage(
                        model = R.drawable.metro1,
                        contentDescription = "Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    // Quick Actions positioned to overlap banner nicely
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 30.dp),
                    ) {
                        QuickActionsSection(
                            navController,
                            userRole = role,
                            onGridItemClick = onGridItemClick,
                        )
                    }
                }
            }

            contentAfterBanner()
        }

        HomeTopBar(
            navController = navController,
            isScrolled = false,
            isAuthenticated = isAuthenticated,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        FloatingUtility(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        )
    }
}

@Composable
fun PhoneOptionButton(
    label: String,
    phoneNumber: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50), // smoother rounded capsule
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp,
        ),
        modifier = Modifier
            .wrapContentWidth()
            .heightIn(min = 60.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.Black.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                ),
                maxLines = 1,
            )
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                ),
            )
        }
    }
}
