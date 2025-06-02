package org.com.hcmurs.ui.screens.metro.qrcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun QRCodeScreen() {
    // Placeholder for QR code screen content
    // You can implement the QR code scanning functionality here
    // For example, using a library like ZXing or ML Kit for barcode scanning
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {},
                floatingActionButton = {
                    FloatingActionButton(onClick = { /* Handle QR code scan */ })

                    {
                        Icon(
                            painter = painterResource(id = org.com.hcmurs.R.drawable.qr_code_scan),
                            contentDescription = "QR scan"
                        )
                    }
                }
            )
        }
        ){
           innerPadding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = org.com.hcmurs.R.drawable.qr_code_scan),
                    contentDescription = "QR Code Placeholder",
                    modifier = Modifier.size(100.dp)
                )
                Text(text = "QR Code Screen",)

            }


        }
}