/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.order.ObjTicketId
import org.com.hcmurs.repositories.apis.order.OrderDaysRepository
import org.com.hcmurs.repositories.apis.order.OrderTicketDaysRequest
import org.com.hcmurs.repositories.apis.payment.OrderStatus
import org.com.hcmurs.repositories.apis.payment.PaymentRepository
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import org.com.hcmurs.repositories.apis.ticket.TicketType

data class OrderInfoUiState(
    val ticketType: TicketType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingOrder: Boolean = false,
    val orderCreationSuccess: Boolean = false,
    val orderCreationMessage: String? = null,
    val isProcessing: Boolean = false,
    val clientSecret: String? = null,
    val processMessage: String? = null,
    val paymentIntentId: String? = null,
    val payOSCheckoutUrl: String? = null,
)

@HiltViewModel
class OrderInfoViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val orderDaysRepository: OrderDaysRepository,
    private val paymentRepository: PaymentRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderInfoUiState())
    val uiState: StateFlow<OrderInfoUiState> = _uiState

    private val ticketId: Int? = savedStateHandle.get<String>("ticketId")?.toIntOrNull()

    init {
        fetchTicketDetails()
    }

    private fun fetchTicketDetails() {
        if (ticketId == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "Ticket ID is missing for order.")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = ticketRepository.getTicketTypes() // Fetch all types
            result.onSuccess { tickets ->
                val foundTicket = tickets.find { it.id == ticketId }
                if (foundTicket != null) {
                    _uiState.value = _uiState.value.copy(ticketType = foundTicket)
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = "Ticket not found for ID: $ticketId")
                }
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(errorMessage = throwable.localizedMessage ?: "Unknown error")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
    fun createOrderForDaysTicket(paymentMethodId: Int) {
        val currentTicketType = uiState.value.ticketType
        if (currentTicketType == null) {
            _uiState.update { it.copy(orderCreationMessage = "Lỗi: Không có thông tin loại vé.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingOrder = true, orderCreationMessage = null) }

            val request = OrderTicketDaysRequest(
                ticketId = ObjTicketId(id = currentTicketType.id),
                paymentMethodId = paymentMethodId,
            )

            val result = orderDaysRepository.createOrderForTicketDays(request)
            result.onSuccess { response ->
                if (response.status == 200 || response.status == 0) {
                    _uiState.update { it.copy(isCreatingOrder = false, orderCreationSuccess = true, orderCreationMessage = response.message) }
                } else {
                    _uiState.update { it.copy(isCreatingOrder = false, orderCreationMessage = "Lỗi: ${response.message}") }
                }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isCreatingOrder = false, orderCreationMessage = "Lỗi: ${throwable.localizedMessage}") }
            }
        }
    }
    fun startCheckoutFlow(paymentMethodId: Int) {
        val currentTicketType = uiState.value.ticketType
        if (currentTicketType == null) {
            _uiState.update { it.copy(processMessage = "Lỗi: Không có thông tin loại vé.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, processMessage = "Đang tạo đơn hàng...") }

            val orderRequest = OrderTicketDaysRequest(
                ticketId = ObjTicketId(id = currentTicketType.id),
                paymentMethodId = paymentMethodId,
            )
            val orderResult = orderDaysRepository.createOrderForTicketDays(orderRequest)

            orderResult.onSuccess { orderResponse ->
                val orderData = orderResponse.data
                if ((orderResponse.status == 200 || orderResponse.status == 0) && orderData != null) {
                    _uiState.update { it.copy(processMessage = "Đang tạo PaymentIntent...") }

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
                                        paymentIntentId = paymentIntentId,
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

    fun startPayOSCheckoutFlow(paymentMethodId: Int) {
        val ticketType = uiState.value.ticketType ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, processMessage = "Đang tạo đơn hàng...") }

            val orderRequest = OrderTicketDaysRequest(ObjTicketId(ticketType.id), paymentMethodId)
            val orderResult = orderDaysRepository.createOrderForTicketDays(orderRequest)

            orderResult.onSuccess { orderResponse ->
                val orderId = orderResponse.data?.orderId
                if (orderId != null) {
                    _uiState.update { it.copy(processMessage = "Đang tạo liên kết thanh toán...") }
                    val payOSResult = paymentRepository.createPaymentLink(orderId)

                    payOSResult.onSuccess { payOSResponse ->
                        _uiState.update { it.copy(isProcessing = false, payOSCheckoutUrl = payOSResponse.data?.checkoutUrl) }
                    }.onFailure {
                        _uiState.update { it.copy(isProcessing = false, processMessage = "Lỗi kết nối khi tạo thanh toán.") }
                    }
                } else {
                    _uiState.update { it.copy(isProcessing = false, processMessage = "Lỗi: Không tạo được đơn hàng.") }
                }
            }.onFailure {
                _uiState.update { it.copy(isProcessing = false, processMessage = "Lỗi kết nối khi tạo đơn hàng.") }
            }
        }
    }

    fun updateOrderStatus(orderCode: Int, status: OrderStatus) {
        viewModelScope.launch {
            paymentRepository.updateOrderStatus(orderCode, status)
        }
    }

    fun clearCheckoutStatus() {
        _uiState.update { it.copy(clientSecret = null, processMessage = null) }
    }
}
