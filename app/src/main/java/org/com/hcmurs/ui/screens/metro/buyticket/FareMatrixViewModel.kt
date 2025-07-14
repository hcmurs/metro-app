package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.FareMatrix
import org.com.hcmurs.FareMatrixResponse
import org.com.hcmurs.repositories.apis.order.CreateOrderRequest
import org.com.hcmurs.repositories.apis.order.CreateOrderResponse
import org.com.hcmurs.repositories.apis.order.FareMatrixIdObject
import org.com.hcmurs.repositories.apis.order.OrderRepository
import org.com.hcmurs.repositories.apis.payment.PaymentRepository
import org.com.hcmurs.repositories.apis.ticket.FareMatrixRepository
import javax.inject.Inject


data class FareMatrixUiState(
    val fareMatrices: List<FareMatrix> = emptyList(),
    val calculatedFare: FareMatrixResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingOrder: Boolean = false,
    val createOrderResponse: CreateOrderResponse? = null,
    val createOrderError: String? = null,
    val isProcessing: Boolean = false,
    val clientSecret: String? = null,
    val processMessage: String? = null,
    val paymentIntentId: String? = null,
)

@HiltViewModel
class FareMatrixViewModel @Inject constructor(
    private val fareMatrixRepository: FareMatrixRepository,
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FareMatrixUiState())
    val uiState: StateFlow<FareMatrixUiState> = _uiState

    init {
        fetchFareMatrices()
    }

    fun fetchFareMatrices() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = fareMatrixRepository.getFareMatrices()
            result.onSuccess { matrices ->
                _uiState.value = _uiState.value.copy(fareMatrices = matrices)
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(errorMessage = throwable.localizedMessage ?: "Unknown error")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)

        }
    }

    fun getFareForStations(entryStationId: Int, exitStationId: Int) {
        viewModelScope.launch {
            _uiState.value = FareMatrixUiState(isLoading = true) // Reset state và hiển thị loading
            val result = fareMatrixRepository.getFareForRoute(entryStationId, exitStationId)
            result.onSuccess { matrix ->
                _uiState.value = FareMatrixUiState(calculatedFare = matrix, isLoading = false)
            }.onFailure { throwable ->
                _uiState.value = FareMatrixUiState(errorMessage = throwable.localizedMessage ?: "Unknown error", isLoading = false)
            }
        }
    }

    fun createSingleOrder(fareMatrixId: Int, paymentMethodId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingOrder = true, createOrderError = null, createOrderResponse = null) }

            val request = CreateOrderRequest(
                fareMatrixId = FareMatrixIdObject(id = fareMatrixId),
                paymentMethodId = paymentMethodId
            )

            val result = orderRepository.createSingleOrder(request)

            result.onSuccess { response ->
                _uiState.update {
                    it.copy(
                        isCreatingOrder = false,
                        createOrderResponse = response
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isCreatingOrder = false,
                        createOrderError = throwable.localizedMessage ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
    fun startSingleTicketCheckoutFlow(paymentMethodId: Int) {
        if (_uiState.value.isProcessing) return

        val currentFareMatrix = uiState.value.calculatedFare?.data
        if (currentFareMatrix == null) {
            _uiState.update { it.copy(processMessage = "Lỗi: Không tìm thấy thông tin giá vé.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, processMessage = "Đang tạo đơn hàng...") }

            val orderRequest = CreateOrderRequest(
                fareMatrixId = FareMatrixIdObject(id = currentFareMatrix.fareMatrixId),
                paymentMethodId = paymentMethodId
            )
            val orderResult = orderRepository.createSingleOrder(orderRequest)

            orderResult.onSuccess { orderResponse ->
                val orderData = orderResponse.data
                if ((orderResponse.status == 200 || orderResponse.status == 0) && orderData != null) {
                    _uiState.update { it.copy(processMessage = "Đang tạo PaymentIntent...") }
                    delay(300)
                    val checkoutResult = paymentRepository.createStripeCheckoutSession(orderData.orderId)

                    checkoutResult.onSuccess { checkoutResponse ->
                        if (checkoutResponse.status == 200) {
                            val clientSecret = checkoutResponse.data?.clientSecret
                            val paymentIntentId = checkoutResponse.data?.sessionId
                            if (!clientSecret.isNullOrBlank() && !paymentIntentId.isNullOrBlank()) {
                                _uiState.update {
                                    it.copy(
                                        isProcessing = false,
                                        processMessage = "Đã sẵn sàng thanh toán",
                                        clientSecret = clientSecret,
                                        paymentIntentId = paymentIntentId
                                    )
                                }
                            } else {
                                _uiState.update { it.copy(isProcessing = false, processMessage = "Dữ liệu phiên không hợp lệ.") }
                            }
                        } else {
                            _uiState.update { it.copy(isProcessing = false, processMessage = "Lỗi tạo phiên thanh toán: ${checkoutResponse.message}") }
                        }
                    }.onFailure {
                        _uiState.update { it.copy(isProcessing = false, processMessage = "Lỗi kết nối khi tạo phiên thanh toán.") }
                    }
                } else {
                    _uiState.update { it.copy(isProcessing = false, processMessage = "Lỗi tạo đơn hàng: ${orderResponse.message}") }
                }
            }.onFailure {
                _uiState.update { it.copy(isProcessing = false, processMessage = "Lỗi kết nối khi tạo đơn hàng.") }
            }
        }
    }

    fun verifyPaymentSuccess() {
        val intentId = uiState.value.paymentIntentId ?: return
        viewModelScope.launch {
            val result = paymentRepository.verifyStripeSuccess(intentId)
            result.onSuccess {
                _uiState.update { it.copy(processMessage = "✅ Thanh toán thành công") }
            }.onFailure {
                _uiState.update { it.copy(processMessage = "❌ Lỗi xác nhận thanh toán thành công") }
            }
        }
    }

    fun verifyPaymentFailed() {
        val intentId = uiState.value.paymentIntentId ?: return
        viewModelScope.launch {
            val result = paymentRepository.verifyStripeFailed(intentId)
            result.onSuccess {
                _uiState.update { it.copy(processMessage = "❌ Thanh toán thất bại") }
            }.onFailure {
                _uiState.update { it.copy(processMessage = "❌ Lỗi xác nhận thất bại") }
            }
        }
    }

    fun clearCreateOrderStatus() {
        _uiState.update { it.copy(clientSecret = null, createOrderError = null) }
    }


}