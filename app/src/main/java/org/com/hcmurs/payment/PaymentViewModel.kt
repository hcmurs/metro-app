package org.com.hcmurs.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.stripe.StripeRepository
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: StripeRepository
) : ViewModel() {
    
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Ready)
    val paymentState: StateFlow<PaymentState> = _paymentState
    
    // For order-based payments
    fun startOrderPayment(orderId: Long) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            try {
                Log.d("API Call", "Making order payment API call for order ID: $orderId")
                val response = repository.createOrderPaymentIntent(orderId)
                Log.d("API Response", "Response: $response")
                
                if (response.status == 200 && response.data != null) {
                    _paymentState.value = PaymentState.ClientSecretReceived(
                        clientSecret = response.data.clientSecret,
                        publishableKey = response.data.publishableKey ?: getPublishableKey(),
                        paymentIntentId = response.data.paymentIntentId
                    )
                } else {
                    _paymentState.value = PaymentState.Error(response.message)
                }
            } catch (e: Exception) {
                Log.e("API Error", "Error: ${e.message}", e)
                _paymentState.value = PaymentState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    // For ticket-based payments
    fun startTicketPayment(ticketId: Long) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            try {
                Log.d("API Call", "Making ticket payment API call for ticket ID: $ticketId")
                val response = repository.createTicketPaymentIntent(ticketId)
                Log.d("API Response", "Response: $response")
                
                if (response.status == 200 && response.data != null) {
                    _paymentState.value = PaymentState.ClientSecretReceived(
                        clientSecret = response.data.clientSecret,
                        publishableKey = response.data.publishableKey ?: getPublishableKey(),
                        paymentIntentId = response.data.paymentIntentId
                    )
                } else {
                    _paymentState.value = PaymentState.Error(response.message)
                }
            } catch (e: Exception) {
                Log.e("API Error", "Error: ${e.message}", e)
                _paymentState.value = PaymentState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    // Confirm payment after PaymentSheet completes
    fun confirmOrderPayment(paymentIntentId: String) {
        viewModelScope.launch {
            try {
                val response = repository.confirmOrderPayment(paymentIntentId)
                if (response.status == 200) {
                    _paymentState.value = PaymentState.Success
                } else {
                    _paymentState.value = PaymentState.Error(response.message)
                }
            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error(e.message ?: "Confirmation failed")
            }
        }
    }
    
    fun confirmTicketPayment(paymentIntentId: String) {
        viewModelScope.launch {
            try {
                val response = repository.confirmTicketPayment(paymentIntentId)
                if (response.status == 200) {
                    _paymentState.value = PaymentState.Success
                } else {
                    _paymentState.value = PaymentState.Error(response.message)
                }
            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error(e.message ?: "Confirmation failed")
            }
        }
    }
    
    private fun getPublishableKey(): String {
        // TODO: Replace with your actual Stripe publishable key
        // This should come from your app configuration or BuildConfig
        return "pk_test_your_publishable_key_here"
    }
    
    fun onPaymentSuccess(paymentIntentId: String = "", isOrderPayment: Boolean = true) {
        // If paymentIntentId is provided, confirm payment status with backend
        if (paymentIntentId.isNotEmpty()) {
            if (isOrderPayment) {
                confirmOrderPayment(paymentIntentId)
            } else {
                confirmTicketPayment(paymentIntentId)
            }
        } else {
            // For backward compatibility - just mark as success
            _paymentState.value = PaymentState.Success
        }
    }
    
    fun onPaymentCancelled() {
        _paymentState.value = PaymentState.Ready
    }
    
    fun onPaymentFailed(errorMessage: String?) {
        _paymentState.value = PaymentState.Error(errorMessage ?: "Payment failed")
    }
    
    fun resetState() {
        _paymentState.value = PaymentState.Ready
    }
    
    // Legacy method for backward compatibility
    @Deprecated("Use startOrderPayment or startTicketPayment instead")
    fun startPayment() {
        // For backward compatibility, show an error message prompting to use specific methods
        _paymentState.value = PaymentState.Error("Please use startOrderPayment() or startTicketPayment() instead")
    }
}

sealed class PaymentState {
    data object Ready : PaymentState()
    data object Loading : PaymentState()
    data object Success : PaymentState()
    class Error(val message: String) : PaymentState()
    class ClientSecretReceived(
        val clientSecret: String?,
        val publishableKey: String?,
        val paymentIntentId: String
    ) : PaymentState()
}