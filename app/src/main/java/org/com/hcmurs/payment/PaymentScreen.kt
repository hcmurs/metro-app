package org.com.hcmurs.payment

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun MomoPaymentScreen(
    navController: NavHostController,
    ticketId: String,
    amount: Long,
    ticketName: String,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val paymentState by viewModel.paymentState.collectAsState()
    val paymentResult by viewModel.paymentResult.collectAsState()

    // Create payment when screen is shown
    LaunchedEffect(Unit) {
        val orderInfo = "Mua vé $ticketName"
       // viewModel.createMomoPayment(amount, orderInfo)
    }

    // Handle payment URL received
    LaunchedEffect(paymentState) {
        if (paymentState is PaymentViewModel.PaymentState.PaymentUrlReceived) {
            val paymentUrl = (paymentState as PaymentViewModel.PaymentState.PaymentUrlReceived).payUrl

            // Open the URL in browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
            context.startActivity(intent)
        }
    }

    // Handle payment result
    LaunchedEffect(paymentResult) {
        paymentResult?.let { result ->
            if (result.successful) {
                // Navigate to success screen
                navController.navigate("payment_success/${result.orderId}")
            } else {
                // Navigate to failure screen
                navController.navigate("payment_failed/${result.message}")
            }
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (paymentState) {
                is PaymentViewModel.PaymentState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Đang khởi tạo thanh toán...")
                    }
                }
                is PaymentViewModel.PaymentState.Error -> {
                    Text(
                        text = (paymentState as PaymentViewModel.PaymentState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is PaymentViewModel.PaymentState.PaymentUrlReceived -> {
                    Text("Đang chuyển đến trang thanh toán MoMo...")
                }
                else -> {
                    Text("Đang xử lý...")
                }
            }
        }
    }
}