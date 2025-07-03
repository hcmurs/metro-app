package org.com.hcmurs.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.stripe.ProductRequest
import org.com.hcmurs.repositories.apis.stripe.StripeRepository
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: StripeRepository
) : ViewModel() {
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Ready)
    val paymentState: StateFlow<PaymentState> = _paymentState

    fun startPayment() {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            try {
                Log.d("API Call", "Making API call...")
                val response = repository.createPaymentIntent(
                    ProductRequest(
                        name = "Sample Product",
                        currency = "usd",
                        amount = 1000L,
                        quantity = 1
                    )
                )
                Log.d("API Response", "Response: $response")

                if (response.status == "SUCCESS") {
                    _paymentState.value = PaymentState.ClientSecretReceived(
                        response.clientSecret,
                        response.publishableKey
                    )
                } else {
                    _paymentState.value = PaymentState.Error(response.message)
                }
            } catch (e: Exception) {
                Log.e("API Error", "Error: ${e.message}", e)
                e.printStackTrace()
                _paymentState.value = PaymentState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onPaymentSuccess() {
        _paymentState.value = PaymentState.Success
    }

    fun onPaymentCancelled() {
        _paymentState.value = PaymentState.Ready
    }

    fun onPaymentFailed(errorMessage: String?) {
        _paymentState.value = PaymentState.Error(errorMessage ?: "Unknown error")
    }
}

sealed class PaymentState {
    data object Ready : PaymentState()
    data object Loading : PaymentState()
    data object Success : PaymentState()
    class Error(val message: String) : PaymentState()
    class ClientSecretReceived(
        val clientSecret: String?,
        val publishableKey: String?
    ) : PaymentState()
}