package org.com.hcmurs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.ui.screens.login.LoginScreen
import org.com.hcmurs.ui.screens.metro.PlaceholderScreen
import org.com.hcmurs.ui.screens.metro.account.AccountScreen
import org.com.hcmurs.ui.screens.metro.cooperationlink.CooperationLinkScreen
import org.com.hcmurs.ui.screens.metro.event.EventScreen
import org.com.hcmurs.ui.screens.metro.feedback.FeedbackScreen
import org.com.hcmurs.ui.screens.metro.home.HomeMetroScreen
import org.com.hcmurs.ui.screens.metro.maps.MapScreen
import org.com.hcmurs.ui.screens.metro.myticket.MyTicketScreen
import org.com.hcmurs.ui.screens.metro.redeemcodeforticket.RedeemCodeForTicketScreen
import org.com.hcmurs.ui.screens.metro.route.RouteScreen
import org.com.hcmurs.ui.screens.metro.ticketinformation.TicketInformationScreen
import org.com.hcmurs.ui.screens.osmap.OsmdroidMapScreen
import org.com.hcmurs.ui.screens.userprofile.ProfileScreen
import org.com.hcmurs.ui.screens.userprofile.ProfileViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object HomeMetro : Screen("homeMetro")
    object Feedback : Screen("feedback")
    object RedeemCodeForTicket : Screen("redeemCodeForTicket")
    object MyTicket : Screen("myTicket")

    // Add new screen routes for the grid items
    object BuyTicket : Screen("buyTicket")
    object BuyTicketDetail : Screen("buyTicketDetail")
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
    object OsmdroidMap : Screen("osmdroidMap")
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

    NavHost(navController = navController, startDestination = Screen.Route.route) {

        composable(Screen.OsmdroidMap.route) {
            OsmdroidMapScreen(navController)
        }

        composable(Screen.UserProfile.route) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(navController = navController, viewModel = profileViewModel)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = hiltViewModel(),
             //   mainViewModel = mainViewModel
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

        composable(Screen.HomeMetro.route) {
            HomeMetroScreen(navController)
        }

        // Add placeholder screens for the new routes
        // Replace these with your actual screen implementations
        composable(Screen.BuyTicket.route) {
            PlaceholderScreen(navController, "Buy Ticket Screen")
        }
        composable(Screen.BuyTicketDetail.route) {
            PlaceholderScreen(navController, "Buy Ticket Detail Screen")
        }
        composable(Screen.Route.route) {
            RouteScreen(navController)
        }

        composable(Screen.Maps.route) {
            MapScreen(navController)
        }

        composable(Screen.VirtualTour.route) {
            PlaceholderScreen(navController, "Virtual Tour Screen")
        }

        composable(Screen.TicketInformation.route) {
            TicketInformationScreen(navController)
        }

        composable(Screen.Account.route) {
            AccountScreen(navController)
        }

        composable(Screen.Event.route) {
            EventScreen(navController)
        }

        composable(Screen.ConstructionImage.route) {
            PlaceholderScreen(navController, "Construction Image Screen")
        }

        composable(Screen.Setting.route) {
            PlaceholderScreen(navController, "Setting Screen")
        }

        composable(Screen.CooperationLink.route) {
            CooperationLinkScreen(navController)
        }

        composable(Screen.Introduction.route) {
            PlaceholderScreen(navController, "Introduction Screen")
        }
    }
}