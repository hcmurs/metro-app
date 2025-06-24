//package org.com.hcmurs.payment
//
//import android.content.Intent
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//class PaymentViewModel @Inject constructor(
//    private val momoPaymentService: MomoPaymentService
//) : ViewModel() {
//    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
//    val paymentState: StateFlow<PaymentState> = _paymentState
//
//    private val _paymentResult = MutableStateFlow<PaymentResult?>(null)
//    val paymentResult: StateFlow<PaymentResult?> = _paymentResult
//
//    fun initiatePayment(amount: Long, ticketInfo: String) {
//        viewModelScope.launch {
//            _paymentState.value = PaymentState.Loading
//            momoPaymentService.createPaymentRequest(amount, ticketInfo).fold(
//                onSuccess = { response ->
//                    _paymentState.value = PaymentState.PaymentUrlReceived(response.payUrl)
//                },
//                onFailure = { error ->
//                    _paymentState.value = PaymentState.Error("Payment request failed: ${error.message}")
//                }
//            )
//        }
//    }
//
//    fun handlePaymentResult(intent: Intent?) {
//        intent?.data?.let { uri ->
//            if (uri.scheme == "metroapp" && uri.host == "payment") {
//                val status = uri.getQueryParameter("status") ?: "failed"
//                val amount = uri.getQueryParameter("amount")?.toLongOrNull() ?: 0
//                val message = uri.getQueryParameter("message") ?: ""
//                val orderId = uri.getQueryParameter("orderId") ?: ""
//
//                _paymentResult.value = PaymentResult(
//                    successful = status == "success",
//                    amount = amount,
//                    message = message,
//                    orderId = orderId
//                )
//            }
//        }
//    }
//
//    sealed class PaymentState {
//        object Idle : PaymentState()
//        object Loading : PaymentState()
//        data class PaymentUrlReceived(val payUrl: String) : PaymentState()
//        data class Error(val message: String) : PaymentState()
//    }
//
//    data class PaymentResult(
//        val successful: Boolean,
//        val amount: Long,
//        val message: String,
//        val orderId: String
//    )
//}