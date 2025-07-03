package org.com.hcmurs.payment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet

@Composable
fun PaymentScreen(
    paymentSheet: PaymentSheet,
    viewModel: PaymentViewModel = hiltViewModel<PaymentViewModel>()
) {
    val context = LocalContext.current
    val paymentState = viewModel.paymentState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (paymentState) {
            is PaymentState.Loading -> CircularProgressIndicator()
            is PaymentState.Success -> Text("Payment Successful!")
            is PaymentState.Error -> {
                Text("Payment Failed: ${paymentState.message}")
            }

            is PaymentState.Ready -> {
                Button(onClick = {
                    viewModel.startPayment()
                }) {
                    Text("Pay Now")
                }
            }

            is PaymentState.ClientSecretReceived -> {
                val state = paymentState
                LaunchedEffect(state.clientSecret, state.publishableKey) {
                    // STEP 1: Initialize PaymentConfiguration FIRST
                    state.publishableKey?.let { publishableKey ->
                        try {
                            PaymentConfiguration.init(context, publishableKey)
                            println("PaymentConfiguration initialized successfully")
                        } catch (e: Exception) {
                            println("Failed to initialize PaymentConfiguration: ${e.message}")
                            viewModel.onPaymentFailed("Failed to initialize payment configuration")
                            return@LaunchedEffect
                        }
                    } ?: run {
                        viewModel.onPaymentFailed("Missing publishable key")
                        return@LaunchedEffect
                    }

                    // STEP 2: Present payment sheet AFTER configuration is initialized
                    state.clientSecret?.let { clientSecret ->
                        try {
                            paymentSheet.presentWithPaymentIntent(
                                paymentIntentClientSecret = clientSecret,
                                configuration = PaymentSheet.Configuration(
                                    merchantDisplayName = "Example, Inc.",
                                    allowsDelayedPaymentMethods = true
                                )
                            )
                            println("Payment sheet presented successfully")
                        } catch (e: Exception) {
                            println("Failed to present payment sheet: ${e.message}")
                            viewModel.onPaymentFailed("Failed to show payment sheet")
                        }
                    } ?: run {
                        viewModel.onPaymentFailed("Missing client secret")
                    }
                }
            }
        }
    }
}