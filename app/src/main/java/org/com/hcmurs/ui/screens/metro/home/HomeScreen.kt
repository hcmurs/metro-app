package org.com.hcmurs.ui.screens.metro.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.constant.UserRole
import org.com.hcmurs.ui.components.common.AppHomeScreen
import org.com.hcmurs.ui.components.featured.FeaturedBlogsSection
import org.com.hcmurs.ui.screens.login.LoginViewModel
import org.com.hcmurs.ui.screens.news.BlogSection
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.com.hcmurs.Screen
import org.com.hcmurs.ui.components.dialog.LoginRequiredDialog


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController,
                loginViewModel: LoginViewModel = hiltViewModel()
)
 {
    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()
    var showLoginDialog by remember { mutableStateOf(false) }

    val checkAuthAndNavigate: (String) -> Unit = { route ->
        if (isAuthenticated) {
            navController.navigate(route)
        } else {
            showLoginDialog = true
        }
    }

    if (showLoginDialog) {
        LoginRequiredDialog (
            onDismissRequest = { showLoginDialog = false },
            onLoginClick = {
                showLoginDialog = false
                navController.navigate(Screen.Login.route)
            }
        )
    }

    AppHomeScreen(
        navController = navController,
        showFloatingButton = true,
        role = UserRole.USER,
        onGridItemClick = { screen ->
            when (screen) {
                Screen.RedeemCodeForTicket.route,
                Screen.BuyTicket.route,
                Screen.MyTicket.route,
                Screen.Feedback.route,
                Screen.Account.route -> checkAuthAndNavigate(screen)
                else -> navController.navigate(screen)
            }
        }
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