package org.com.hcmurs.ui.components.common

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.com.hcmurs.R
import org.com.hcmurs.constant.UserRole
import org.com.hcmurs.ui.components.floatingButton.FloatingButton
import org.com.hcmurs.ui.components.quickaction.QuickActionsSection
import org.com.hcmurs.ui.components.topbar.HomeTopBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppHomeScreen(
    navController: NavHostController,
    showFloatingButton: Boolean = true,
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

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE8F5E8), Color.White)
                    )
                ),
            contentPadding = PaddingValues(top = 0.dp, bottom = 240.dp)
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
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 30.dp)
                    ) {
                        QuickActionsSection(navController, userRole = role,onGridItemClick = onGridItemClick )
                    }
                }
            }

            contentAfterBanner()
        }

        HomeTopBar(
            navController = navController,
            isScrolled = isScrolled,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (showFloatingButton) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 50.dp, end = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        PhoneOptionButton("1900 1234") {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:19001234"))
                            context.startActivity(intent)
                            expanded = false
                        }

                        PhoneOptionButton("028 5678") {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0285678"))
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
                    contentDescription = "Phone Icon"
                )
            }
        }
    }
}

@Composable
fun PhoneOptionButton(
    phoneNumber: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(180.dp)
            .height(48.dp)
    ) {
        Text(text = phoneNumber)
    }
}
