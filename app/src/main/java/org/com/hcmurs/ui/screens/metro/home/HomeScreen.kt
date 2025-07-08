package org.com.hcmurs.ui.screens.metro.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.constant.UserRole
import org.com.hcmurs.ui.components.common.AppHomeScreen
import org.com.hcmurs.ui.components.featured.FeaturedBlogsSection
import org.com.hcmurs.ui.screens.news.BlogSection

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController) {
    AppHomeScreen(
        navController = navController,
        showFloatingButton = true,
        role = UserRole.USER
        ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = Modifier.padding(start = 16.dp)) {
                FeaturedBlogsSection(navController)
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.padding(start = 16.dp)) {
                BlogSection(navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeMetroScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}