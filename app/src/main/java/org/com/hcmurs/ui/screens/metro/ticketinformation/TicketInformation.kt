/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.metro.ticketinformation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.R
import org.com.hcmurs.model.TicketInformation
import org.com.hcmurs.ui.components.card.ticketinformation.TicketInformationCard
import org.com.hcmurs.ui.components.topbar.TicketInformationTopBar

@Composable
fun getTicketList(): List<TicketInformation> = listOf(
    TicketInformation(
        id = "1",
        title = stringResource(R.string.ticket_title_1),
        description = stringResource(R.string.ticket_desc_1),
        date = stringResource(R.string.ticket_time_1),
    ),
    TicketInformation(
        id = "2",
        title = stringResource(R.string.ticket_title_2),
        description = stringResource(R.string.ticket_desc_2),
        date = stringResource(R.string.ticket_time_2),
    ),
    TicketInformation(
        id = "3",
        title = stringResource(R.string.ticket_title_3),
        description = stringResource(R.string.ticket_desc_3),
        date = stringResource(R.string.ticket_time_3),
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketInformationScreen(navController: NavHostController) {
    // Sample events data - empty for demonstration
//    val events = remember { emptyList<Event>() }

    val events = getTicketList()

    Scaffold(
        topBar = {
            TicketInformationTopBar(
                navController = navController,
            )
        },
        containerColor = Color.White,
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            if (events.isEmpty()) {
                // Fixed empty state with vertical arrangement
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hurc),
                        contentDescription = "HURC Logo",
                        modifier = Modifier.size(120.dp),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.no_data),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                }
            } else {
                // Show event list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(events) { event ->
                        TicketInformationCard(event)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketInformationPreview() {
    TicketInformationScreen(navController = rememberNavController())
}
