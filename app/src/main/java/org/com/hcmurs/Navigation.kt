package org.com.hcmurs

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.com.hcmurs.ui.screens.addoredit.AddOrEditScreen
import org.com.hcmurs.ui.screens.detail.DetailScreen
import org.com.hcmurs.ui.screens.home.HomeScreen
import org.com.hcmurs.ui.screens.home.HomeViewModel
import org.com.hcmurs.ui.screens.login.LoginScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Detail : Screen("detail")
    object AddOrEdit : Screen("addOrEdit")
    object HomeMetro: Screen("homeMetro")
}

@Composable
fun Navigation(
    authResultLauncher: ActivityResultLauncher<Intent>? = null,
    setAuthResultCallback: ((Intent?) -> Unit) -> Unit = {}
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val mainState = mainViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(mainState.value.error) {
        if (mainState.value.error.isNotEmpty()) {
            Toast.makeText(context, mainState.value.error, Toast.LENGTH_LONG).show()
            mainViewModel.setError("")
        }
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                mainViewModel = mainViewModel,
                authResultLauncher = authResultLauncher,
                setAuthResultCallback = setAuthResultCallback
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController, viewModel = hiltViewModel<HomeViewModel>(), mainViewModel)
        }

        composable(
            Screen.Detail.route + "?noteIndex={noteIndex}",
            arguments = listOf(
                navArgument("noteIndex") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val noteIndex = backStackEntry.arguments?.getInt("noteIndex") ?: -1
            val parentEntry = navController.getBackStackEntry(Screen.Home.route)
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            DetailScreen(navController, homeViewModel, mainViewModel, noteIndex)
        }

        composable(
            Screen.AddOrEdit.route + "?noteIndex={noteIndex}",
            arguments = listOf(
                navArgument("noteIndex") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val noteIndex = backStackEntry.arguments?.getInt("noteIndex") ?: -1
            val parentEntry = navController.getBackStackEntry(Screen.Home.route)
            val homeViewModel: HomeViewModel = hiltViewModel(parentEntry)

            AddOrEditScreen(
                navController,
                homeViewModel,
                hiltViewModel(),
                mainViewModel,
                noteIndex
            )
        }
    }
}