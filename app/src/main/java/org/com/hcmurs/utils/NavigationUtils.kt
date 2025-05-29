package org.com.hcmurs.utils

import androidx.navigation.NavGraph.Companion.findStartDestination
import org.com.hcmurs.Screen
import org.com.hcmurs.constant.ScreenTitle

// Helper function to map ScreenTitle to navigation routes
fun getNavigationRoute(screenTitle: ScreenTitle): String {
    return when (screenTitle) {
        ScreenTitle.MY_TICKET -> Screen.MyTicket.route
        ScreenTitle.REDEEM_CODE_FOR_TICKET -> Screen.RedeemCodeForTicket.route
        ScreenTitle.FEEDBACK -> Screen.Feedback.route
        ScreenTitle.BUY_TICKET -> Screen.BuyTicket.route
        ScreenTitle.ROUTE -> Screen.Route.route
        ScreenTitle.MAPS -> Screen.Maps.route
        ScreenTitle.VIRTUAL_TOUR -> Screen.VirtualTour.route
        ScreenTitle.TICKET_INFORMATION -> Screen.TicketInformation.route
        ScreenTitle.ACCOUNT -> Screen.Account.route
        ScreenTitle.EVENT -> Screen.Event.route
        ScreenTitle.CONSTRUCTION_IMAGE -> Screen.ConstructionImage.route
        ScreenTitle.SETTING -> Screen.Setting.route
        ScreenTitle.COOPERATION_LINK -> Screen.CooperationLink.route
        ScreenTitle.INTRODUCTION -> Screen.Introduction.route
    }
}

fun navigateToHome(navController: androidx.navigation.NavController) {
    navController.navigate(Screen.HomeMetro.route)
//    {
//        popUpTo(navController.graph.findStartDestination().id) {
//            saveState = true
//        }
//        launchSingleTop = true
//        restoreState = true
//    }
}