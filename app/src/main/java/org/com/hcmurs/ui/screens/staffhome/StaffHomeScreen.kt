package org.com.hcmurs.ui.screens.staffhome

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import org.com.hcmurs.R
import org.com.hcmurs.ui.components.floatingButton.FloatingButton
import org.com.hcmurs.ui.components.quickaction.StaffAccountQuickAccess
import org.com.hcmurs.ui.components.topbar.HomeTopBar
import org.com.hcmurs.ui.screens.login.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StaffHomeScreen(
    navController: NavHostController,
) {
    val listState = rememberLazyListState()

    val viewModel: LoginViewModel = hiltViewModel()
    val userProfile by viewModel.userProfile.collectAsState()

    if (viewModel == null) {
        // Simple placeholder UI when no viewModel is provided
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Account information unavailable")
        }
        return
    }

    val userName = userProfile?.name ?: "Chưa cập nhật"

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
                Box(modifier = Modifier.height(400.dp)) {
                    AsyncImage(
                        model = R.drawable.login_banner,
                        contentDescription = "Staff Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 20.dp)
                    ) {
                        StaffQRSection(navController = navController)
                    }
                }
            }
            item {
                StaffAccountQuickAccess(
                    navController = navController,
                    userName = userName
                )
            }
            // Có thể thêm các content khác cho staff ở đây
            // item {
            //     // Staff-specific content
            // }
        }

        HomeTopBar(
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Optional: Floating button cho staff nếu cần
        FloatingButton(
            onClick = {
                Log.d("StaffFloatingButton", "Staff action!")
            },
            icon = Icons.Default.Phone,
            contentDescription = "Staff Phone",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 50.dp, end = 16.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun StaffHomeScreenPreview() {
    val navController = rememberNavController()
    StaffHomeScreen(navController = navController)
}
