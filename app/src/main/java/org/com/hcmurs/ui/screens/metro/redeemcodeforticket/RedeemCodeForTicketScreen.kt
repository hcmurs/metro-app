package org.com.hcmurs.ui.screens.metro.redeemcodeforticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.ui.screens.metro.home.HomeTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemCodeForTicketScreen(navController: NavHostController) {
    var textFieldContent by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar() },
        modifier = Modifier.background(Color.Cyan)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Mã vé", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    TextField(
                        value = textFieldContent,
                        onValueChange = { textFieldContent = it },
                        label = { Text("Nhập mã vé") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )

                    Button(
                        onClick = { /* Handle redeem code */ },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .background(Color.Blue)
                            .fillMaxWidth()
                    ) {
                        Text("Redeem Code")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                Text("Lưu ý", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("1. Mã vé chỉ sử dụng một lần.")
                Text("2. Vui lòng nhập đúng mã vé.")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Redeem",
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { /* Handle back navigation */ },
                content = {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue
        )

    )
}

@Preview(showBackground = true)
@Composable
fun RedeemCodeForTicketScreenPreview() {
    val navController = rememberNavController()
    RedeemCodeForTicketScreen(navController = navController)
}
