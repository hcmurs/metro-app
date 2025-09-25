/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.common

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.com.hcmurs.R
import org.com.hcmurs.constant.UserRole
import org.com.hcmurs.ui.components.floatingButton.FloatingButton
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
    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset > 0 }
    }

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()

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
                Box(modifier = Modifier.height(450.dp)) {
                    AsyncImage(
                        model = R.drawable.metro1,
                        contentDescription = "Social link",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 30.dp),
                    ) {
                        QuickActionsSection(navController, userRole = role, onGridItemClick = onGridItemClick)
                    }
                }
            }

            contentAfterBanner()
        }

        HomeTopBar(
            navController = navController,
            isScrolled = isScrolled,
            isAuthenticated = isAuthenticated,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        if (isAuthenticated) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 50.dp, end = 16.dp),
                horizontalAlignment = Alignment.End,
            ) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.End,
                    ) {
                        PhoneOptionButton("Lịch trình và thời gian tàu chạy", "028 7300 6659") {
                            val intent = Intent(Intent.ACTION_DIAL, "tel:02873006659".toUri())
                            context.startActivity(intent)
                            expanded = false
                        }

                        PhoneOptionButton("Vé và các dịch vụ hành khách", "028 7300 3885") {
                            val intent = Intent(Intent.ACTION_DIAL, "tel:02873003885".toUri())
                            context.startActivity(intent)
                            expanded = false
                        }
                    }
                }

                FloatingButton(
                    onClick = {
                        expanded = !expanded
                        Log.d("FloatingButton", "Toggled menu: $expanded")
                    },
                    icon = Icons.Default.Phone,
                    contentDescription = "Phone Icon",
                )
            }
        }
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
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White,
        ),
        modifier = Modifier
            .width(250.dp)
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp)), // Shadow cho hiệu ứng nổi
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
            Text(text = phoneNumber, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhoneOptionButtonPreview() {
    PhoneOptionButton(
        phoneNumber = "028 7300 6659",
        label = "Lịch trình và thời gian tàu chạy",
    ) {
        // Handle click
    }
}
