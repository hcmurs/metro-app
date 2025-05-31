package org.com.hcmurs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
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
import org.com.hcmurs.ui.screens.metro.PlaceholderScreen
import org.com.hcmurs.ui.screens.metro.account.AccountScreen
import org.com.hcmurs.ui.screens.metro.feedback.FeedbackScreen
import org.com.hcmurs.ui.screens.metro.home.HomeMetroScreen
import org.com.hcmurs.ui.screens.metro.myticket.MyTicketScreen
import org.com.hcmurs.ui.screens.metro.redeemcodeforticket.RedeemCodeForTicketScreen
import org.com.hcmurs.ui.screens.userprofile.ProfileScreen
import org.com.hcmurs.ui.screens.userprofile.ProfileViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Detail : Screen("detail")
    object AddOrEdit : Screen("addOrEdit")
    object HomeMetro : Screen("homeMetro")
    object Feedback : Screen("feedback")
    object RedeemCodeForTicket : Screen("redeemCodeForTicket")
    object MyTicket : Screen("myTicket")

    // Add new screen routes for the grid items
    object BuyTicket : Screen("buyTicket")
    object Route : Screen("route")
    object Maps : Screen("maps")
    object VirtualTour : Screen("virtualTour")
    object TicketInformation : Screen("ticketInformation")
    object Account : Screen("account")
    object Event : Screen("event")
    object ConstructionImage : Screen("constructionImage")
    object Setting : Screen("setting")
    object CooperationLink : Screen("cooperationLink")
    object Introduction : Screen("introduction")

    // Test
    object UserProfile : Screen("userProfile")
}

@SuppressLint("UnrememberedGetBackStackEntry")
@RequiresApi(Build.VERSION_CODES.O)
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

    NavHost(navController = navController, startDestination = Screen.UserProfile.route) {

        composable(Screen.UserProfile.route) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(navController = navController, viewModel = profileViewModel)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                mainViewModel = mainViewModel,
                authResultLauncher = authResultLauncher,
                setAuthResultCallback = setAuthResultCallback
            )
        }

        composable(Screen.RedeemCodeForTicket.route) {
            RedeemCodeForTicketScreen(navController)
        }

        composable(Screen.MyTicket.route) {
            MyTicketScreen(navController)
        }

        composable(Screen.Feedback.route) {
            FeedbackScreen(navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController, viewModel = hiltViewModel<HomeViewModel>(), mainViewModel)
        }

        composable(Screen.HomeMetro.route) {
            HomeMetroScreen(navController)
        }

        // Add placeholder screens for the new routes
        // Replace these with your actual screen implementations
        composable(Screen.BuyTicket.route) {
            PlaceholderScreen(navController, "Buy Ticket Screen")
        }

        composable(Screen.Route.route) {
            PlaceholderScreen(navController, "Route Screen")
        }

        composable(Screen.Maps.route) {
            PlaceholderScreen(navController, "Maps Screen")
        }

        composable(Screen.VirtualTour.route) {
            PlaceholderScreen(navController, "Virtual Tour Screen")
        }

        composable(Screen.TicketInformation.route) {
            PlaceholderScreen(navController, "Ticket Information Screen")
        }

        composable(Screen.Account.route) {
            AccountScreen(navController)
        }

        composable(Screen.Event.route) {
            PlaceholderScreen(navController, "Event Screen")
        }

        composable(Screen.ConstructionImage.route) {
            PlaceholderScreen(navController, "Construction Image Screen")
        }

        composable(Screen.Setting.route) {
            PlaceholderScreen(navController, "Setting Screen")
        }

        composable(Screen.CooperationLink.route) {
            PlaceholderScreen(navController, "Cooperation Link Screen")
        }

        composable(Screen.Introduction.route) {
            PlaceholderScreen(navController, "Introduction Screen")
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
            val parentEntry = navController.getBackStackEntry(Screen.HomeMetro.route)
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
            val parentEntry = navController.getBackStackEntry(Screen.HomeMetro.route)
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