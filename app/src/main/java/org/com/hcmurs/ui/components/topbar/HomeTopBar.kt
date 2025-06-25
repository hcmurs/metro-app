package org.com.hcmurs.ui.components.topbar

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
import org.com.hcmurs.ui.components.WeatherDisplay
import org.com.hcmurs.ui.screens.metro.home.LanguageDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(isScrolled: Boolean) {
    var selectedLanguage by remember { mutableStateOf("Vietnamese") }
    // Weather data - would come from ViewModel in real app
    val temperature = remember { mutableDoubleStateOf(27.5) }
//    val windSpeed = remember { mutableStateOf(3.2) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherDisplay(
                    temperature = temperature.value,
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
                            tint = if (isScrolled) Color(0xFF4CAF50) else Color.Transparent
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
            containerColor = if (isScrolled) Color(0xFF4CAF50) else Color.Transparent
        ),
        modifier = if (isScrolled) Modifier.shadow(4.dp) else Modifier
    )
}