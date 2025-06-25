package org.com.hcmurs.ui.screens.staffhome

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.constant.UserRole
import org.com.hcmurs.ui.components.common.AppHomeScreen

@Composable
fun StaffHomeScreen(navController: NavHostController) {
    AppHomeScreen(
        navController = navController,
        showFloatingButton = false,
        role = UserRole.STAFF
    ) {
    }
}


@Preview(showBackground = true)
@Composable
fun StaffHomeScreenPreview() {
    val navController = rememberNavController()
    StaffHomeScreen(navController = navController)
}