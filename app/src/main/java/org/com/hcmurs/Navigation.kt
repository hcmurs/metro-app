package org.com.hcmurs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.com.hcmurs.payment.PaymentScreen
import org.com.hcmurs.payment.PaymentViewModel
import org.com.hcmurs.ui.screens.login.LoginScreen
import org.com.hcmurs.ui.screens.login.LoginViewModel
import org.com.hcmurs.ui.screens.metro.PlaceholderScreen
import org.com.hcmurs.ui.screens.metro.account.AccountScreen
import org.com.hcmurs.ui.screens.metro.account.CCCDScreen
import org.com.hcmurs.ui.screens.metro.account.LinkCCCDScreen
import org.com.hcmurs.ui.screens.metro.account.RegisterFormScreen
import org.com.hcmurs.ui.screens.metro.buyticket.BuyTicketScreen
import org.com.hcmurs.ui.screens.metro.buyticket.FareMatrixViewModel
import org.com.hcmurs.ui.screens.metro.buyticket.OrderInfoScreen
import org.com.hcmurs.ui.screens.metro.buyticket.TicketDetailScreen
import org.com.hcmurs.ui.screens.metro.cooperationlink.CooperationLinkScreen
import org.com.hcmurs.ui.screens.metro.event.EventScreen
import org.com.hcmurs.ui.screens.metro.feedback.FeedbackScreen
import org.com.hcmurs.ui.screens.metro.home.HomeScreen
import org.com.hcmurs.ui.screens.metro.maps.MapScreen
import org.com.hcmurs.ui.screens.metro.myticket.MyTicketScreen
import org.com.hcmurs.ui.screens.metro.myticket.TicketQRCodeScreen
import org.com.hcmurs.ui.screens.metro.redeemcodeforticket.RedeemCodeForTicketScreen
import org.com.hcmurs.ui.screens.metro.route.RouteScreen
import org.com.hcmurs.ui.screens.metro.ticketinformation.TicketInformationScreen
import org.com.hcmurs.ui.screens.osmap.OsmdroidMapScreen
import org.com.hcmurs.ui.screens.scanqr.ActionType
import org.com.hcmurs.ui.screens.scanqr.ScanQRScreen
import org.com.hcmurs.ui.screens.scanqr.ScanQRViewModel
import org.com.hcmurs.ui.screens.staffhome.StaffAccountScreen
import org.com.hcmurs.ui.screens.staffhome.StaffHomeScreen
import org.com.hcmurs.ui.screens.stationselection.CalculatedFareScreen
import org.com.hcmurs.ui.screens.stationselection.OrderFareInfoScreen
import org.com.hcmurs.ui.screens.stationselection.StaffStationSelectionScreen
import org.com.hcmurs.ui.screens.stationselection.StationSelectionScreen
import org.com.hcmurs.ui.screens.stationselection.StationSelectionViewModel


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object StaffHomeScreen : Screen("staffHomeScreen")

    object StaffStationSelectionScreen : Screen("stationSelect/{actionType}") {
        fun createRoute(actionType: ActionType): String {
            return "stationSelect/${actionType.name}"
        }
    }

    object Feedback : Screen("feedback")
    object RedeemCodeForTicket : Screen("redeemCodeForTicket")
    object MyTicket : Screen("myTicket")
    object StaffAccount : Screen("staffAccount")

    // Add new screen routes for the grid items
    object BuyTicket : Screen("buyTicket")
    object BuyTicketDetail : Screen("buyTicketDetail/{ticketId}") {
        fun createRoute(ticketId: Int) = "buyTicketDetail/$ticketId"
    }

    object OrderInfo : Screen("orderInfo/{ticketId}") {
        fun createRoute(ticketId: Int) = "orderInfo/$ticketId"
    }

    object CalculatedFare : Screen("calculatedFare/{entryStationId}/{exitStationId}") {
        fun createRoute(entryStationId: Int, exitStationId: Int) =
            "calculatedFare/$entryStationId/$exitStationId"
    }

    object OrderFareInfo : Screen("orderFareInfo/{entryStationId}/{exitStationId}") {
        fun createRoute(entryStationId: Int, exitStationId: Int) =
            "orderFareInfo/$entryStationId/$exitStationId"
    }

    object TicketQRCode : Screen("ticket_qr_code/{ticketCode}") {
        fun createRoute(ticketCode: String) = "ticket_qr_code/$ticketCode"
    }

    object TicketFlow : Screen("ticket_flow")

    object Route : Screen("route")
    object Maps : Screen("maps")
    object VirtualTour : Screen("virtualTour")
    object TicketInformation : Screen("ticketInformation")
    object Account : Screen("account")
    object CCCD : Screen("cccd")
    object RegisterCCCD : Screen("registerCCCD")
    object LinkCCCD : Screen("linkCCCD")
    object Event : Screen("event")
    object ConstructionImage : Screen("constructionImage")
    object Setting : Screen("setting")
    object CooperationLink : Screen("cooperationLink")
    object Introduction : Screen("introduction")
    object StationSelection : Screen("stationSelect")
    object ScanQrCode : Screen("scanQR/{stationId}/{stationName}") {
        fun createRoute(stationId: Int, stationName: String) =
            "scanQR/$stationId/$stationName"

        const val defaultRoute = "scanQR/0/None"
    }

    // Staff Only Choose Station and do scan
    data object StaffScanQrCode : Screen("scanQR/{stationId}/{stationName}") {
        fun createRoute(stationId: Int, stationName: String) = "scanQR/$stationId/$stationName"
        const val defaultRoute = "scanQR/0/None"
    }

    object PaymentScreen : Screen("paymentScreen")

    // Test
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

    val loginViewModel: LoginViewModel = hiltViewModel()
    val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()
    LaunchedEffect(mainState.value.error) {
        if (mainState.value.error.isNotEmpty()) {
            Toast.makeText(context, mainState.value.error, Toast.LENGTH_LONG).show()
            mainViewModel.setError("")
        }
    }
    //val startDestination = if (isAuthenticated) Screen.Account.route else Screen.Login.route

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.OsmdroidMap.route) {
            OsmdroidMapScreen(navController)
        }
        composable(Screen.StaffHomeScreen.route)
        {
            // StaffHomeScreen(navController)
            StaffHomeScreen(navController) // Temporarily using HomeScreen for staff
        }

        composable(
            route = Screen.StaffStationSelectionScreen.route,
            arguments = listOf(
                navArgument("actionType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val actionTypeString = backStackEntry.arguments?.getString("actionType")
            val actionType = try {
                ActionType.valueOf(actionTypeString ?: ActionType.ENTRY.name)
            } catch (e: IllegalArgumentException) {
                ActionType.ENTRY // fallback
            }

            StaffStationSelectionScreen(
                navController = navController,
                stationViewModel = hiltViewModel<StationSelectionViewModel>(),
                actionType = actionType
            )
        }

        composable(Screen.StaffAccount.route) {
            StaffAccountScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel,
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
        // Luồng mua vé đơn
        navigation(
            route = Screen.TicketFlow.route,
            startDestination = Screen.StationSelection.route
        ) {
            // Màn hình 1: Chọn ga
            composable(route = Screen.StationSelection.route) { backStackEntry ->
                val parentEntry =
                    remember(backStackEntry) { navController.getBackStackEntry(Screen.TicketFlow.route) }
                val fareMatrixViewModel: FareMatrixViewModel = hiltViewModel(parentEntry)
                // CẢI TIẾN: Chia sẻ cả StationSelectionViewModel
                val stationViewModel: StationSelectionViewModel = hiltViewModel(parentEntry)
                StationSelectionScreen(
                    navController = navController,
                    stationViewModel = stationViewModel,
                    fareMatrixViewModel = fareMatrixViewModel
                )
            }

            // Màn hình 2: Chi tiết giá vé
            composable(
                route = Screen.CalculatedFare.route,
                arguments = listOf(
                    navArgument("entryStationId") { type = NavType.IntType },
                    navArgument("exitStationId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val parentEntry =
                    remember(backStackEntry) { navController.getBackStackEntry(Screen.TicketFlow.route) }
                val fareMatrixViewModel: FareMatrixViewModel = hiltViewModel(parentEntry)
                val stationViewModel: StationSelectionViewModel = hiltViewModel(parentEntry)
                val entryId = backStackEntry.arguments?.getInt("entryStationId") ?: 0
                val exitId = backStackEntry.arguments?.getInt("exitStationId") ?: 0
                CalculatedFareScreen(
                    navController = navController,
                    entryStationId = entryId,
                    exitStationId = exitId,
                    viewModel = fareMatrixViewModel,
                    stationViewModel = stationViewModel
                )
            }

            // Màn hình 3: THÔNG TIN ĐƠN HÀNG
            composable(
                route = Screen.OrderFareInfo.route,
                arguments = listOf(
                    navArgument("entryStationId") { type = NavType.IntType },
                    navArgument("exitStationId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val parentEntry =
                    remember(backStackEntry) { navController.getBackStackEntry(Screen.TicketFlow.route) }
                val fareMatrixViewModel: FareMatrixViewModel = hiltViewModel(parentEntry)
                val stationViewModel: StationSelectionViewModel = hiltViewModel(parentEntry)
                val entryId = backStackEntry.arguments?.getInt("entryStationId") ?: 0
                val exitId = backStackEntry.arguments?.getInt("exitStationId") ?: 0

                OrderFareInfoScreen(
                    navController = navController,
                    entryStationId = entryId,
                    exitStationId = exitId,
                    fareMatrixViewModel = fareMatrixViewModel,
                    stationViewModel = stationViewModel
                )
            }
        }

        composable(
            route = "paymentScreen?orderId={orderId}&ticketId={ticketId}",
            arguments = listOf(
                navArgument("orderId") { 
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("ticketId") { 
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val orderIdStr = backStackEntry.arguments?.getString("orderId")
            val ticketIdStr = backStackEntry.arguments?.getString("ticketId")
            val orderId = orderIdStr?.toLongOrNull()
            val ticketId = ticketIdStr?.toLongOrNull()

            PaymentScreen(
                orderId = orderId,
                ticketId = ticketId
            )
        }

        composable(
            route = Screen.TicketQRCode.route,
            arguments = listOf(navArgument("ticketCode") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketCode = backStackEntry.arguments?.getString("ticketCode") ?: ""
            TicketQRCodeScreen(navController = navController, ticketCode = ticketCode)
        }
        composable(
            route = "scanQR/{stationId}/{stationName}/{actionType}",
            arguments = listOf(
                navArgument("stationId") { type = NavType.IntType },
                navArgument("stationName") { type = NavType.StringType },
                navArgument("actionType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val stationId = backStackEntry.arguments?.getInt("stationId") ?: 0
            val stationName = backStackEntry.arguments?.getString("stationName") ?: ""
            val actionTypeString = backStackEntry.arguments?.getString("actionType")

            val actionType = try {
                ActionType.valueOf(actionTypeString ?: ActionType.ENTRY.name)
            } catch (e: IllegalArgumentException) {
                ActionType.ENTRY
            }

            val viewModel: ScanQRViewModel = hiltViewModel()
            ScanQRScreen(
                navController,
                stationId,
                stationName,
                viewModel,
                actionType
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        // Add placeholder screens for the new routes
        // Replace these with your actual screen implementations
        composable(Screen.BuyTicket.route) {
            BuyTicketScreen(navController)
        }
        composable(Screen.BuyTicketDetail.route) {
            TicketDetailScreen(navController)
        }
        composable(Screen.OrderInfo.route) {
            OrderInfoScreen(navController)
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
            AccountScreen(
                navController,
                viewModel = loginViewModel
            )
        }

        composable(Screen.CCCD.route) {
            CCCDScreen(navController)
        }
        composable(Screen.RegisterCCCD.route) {
            RegisterFormScreen(navController)
        }

        composable(Screen.LinkCCCD.route) {
            LinkCCCDScreen(navController)
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