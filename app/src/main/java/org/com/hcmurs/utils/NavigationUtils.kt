/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.utils

import org.com.hcmurs.Screen
import org.com.hcmurs.core.constant.ScreenTitle

// Helper function to map ScreenTitle to navigation routes
fun getNavigationRoute(screenTitle: ScreenTitle): String = when (screenTitle) {
    ScreenTitle.MY_TICKET -> Screen.MyTicket.route
    ScreenTitle.REDEEM_CODE_FOR_TICKET -> Screen.RedeemCodeForTicket.route
    ScreenTitle.FEEDBACK -> Screen.Feedback.route
    ScreenTitle.BUY_TICKET -> Screen.BuyTicket.route
    ScreenTitle.ROUTE -> Screen.Route.route
    ScreenTitle.MAPS -> Screen.Maps.route
    ScreenTitle.VIRTUAL_TOUR -> Screen.VirtualTour.route
    ScreenTitle.CHATBOT -> Screen.Chatbot.route
    ScreenTitle.TICKET_INFORMATION -> Screen.TicketInformation.route
    ScreenTitle.ACCOUNT -> Screen.Account.route
    ScreenTitle.EVENT -> Screen.Event.route
    ScreenTitle.CONSTRUCTION_IMAGE -> Screen.ConstructionImage.route
    ScreenTitle.SETTING -> Screen.Setting.route
    ScreenTitle.COOPERATION_LINK -> Screen.CooperationLink.route
    ScreenTitle.INTRODUCTION -> Screen.Introduction.route
    ScreenTitle.SCAN_QR_CODE -> Screen.ScanQrCode.route
}

fun navigateToHome(navController: androidx.navigation.NavController) {
    navController.navigate(Screen.Home.route)
//    {
//        popUpTo(navController.graph.findStartDestination().id) {
//            saveState = true
//        }
//        launchSingleTop = true
//        restoreState = true
//    }
}
