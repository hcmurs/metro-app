package org.com.hcmurs.ui.screens.metro.redeemcodeforticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.com.hcmurs.Screen
import org.com.hcmurs.utils.navigateToHome

val PrimaryGreen = Color(0xFF2E7D32)
val ErrorRed = Color(0xFFD32F2F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemCodeForTicketScreen(navController: NavHostController) {
    var textFieldContent by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar(navController) },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Label with red asterisk
            Text(
                text = buildAnnotatedString {
                    append("Mã vé")
                    append(" ")
                    pushStyle(SpanStyle(color = ErrorRed))
                    append("*")
                    pop()
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = textFieldContent,
                onValueChange = {
                    textFieldContent = it
                    if (isError && it.isNotBlank()) isError = false
                },
                isError = isError,
                label = { Text("Nhập mã vé") },
                trailingIcon = {
                    IconButton(onClick = {
                        // TODO: trigger QR scan logic here
                    }) {
                        Icon(
                            imageVector = Icons.Filled.QrCodeScanner,
                            contentDescription = "Scan QR",
                            tint = PrimaryGreen
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            if (isError) {
                Text(
                    text = "Vui lòng nhập mã vé.",
                    color = ErrorRed,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }

            Button(
                onClick = {
                    if (textFieldContent.isBlank()) {
                        isError = true
                    } else {
                        // TODO: Handle redeem logic
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Redeem Code", color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Lưu ý", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("1. Mã vé chỉ sử dụng một lần.")
            Text("2. Vui lòng nhập đúng mã vé.")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Redeem",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {  navigateToHome(navController) }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryGreen
        )
    )
}

@Preview(showBackground = true)
@Composable
fun RedeemCodeForTicketScreenPreview() {
    RedeemCodeForTicketScreen(navController = rememberNavController())
}
