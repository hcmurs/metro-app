package org.com.hcmurs.ui.components.topbar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.com.hcmurs.ui.components.weather.WeatherDisplay
import org.com.hcmurs.ui.components.dropdown.LanguageDropdown
import org.com.hcmurs.ui.components.weather.WeatherViewModel
import org.com.hcmurs.ui.theme.GreenPrimary

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isScrolled: Boolean = false,
    modifier: Modifier = Modifier // Add modifier parameter
) {
    var selectedLanguage by remember { mutableStateOf("Vietnamese") }
    // Weather data - would come from ViewModel in real app
    val weatherViewModel: WeatherViewModel = hiltViewModel<WeatherViewModel>()

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherDisplay(
                    viewModel = weatherViewModel,
                    isScrolled = isScrolled
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Thông báo",
                            // Show notification icon in white when not scrolled for better visibility over image
                            tint = Color.White
                        )
                    }
                    LanguageDropdown(
                        selectedLanguage,
                        onLanguageChange = { selectedLanguage = it },
                        isScrolled = isScrolled
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            // Use semi-transparent background when not scrolled for floating effect
            containerColor = if (isScrolled) {
                GreenPrimary
            } else {
                Color.Black.copy(alpha = 0.3f) // Semi-transparent overlay
            }
        ),
        modifier = modifier.then(
            if (isScrolled) Modifier.shadow(4.dp) else Modifier
        )
    )
}