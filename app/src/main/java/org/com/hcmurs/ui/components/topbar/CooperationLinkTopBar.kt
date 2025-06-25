package org.com.hcmurs.ui.components.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.com.hcmurs.ui.components.common.CommonTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CooperationLinkTopBar(navController: NavController) {
    CommonTopBar(
        navController = navController,
        title = "Liên kết hợp tác",
    )
}