package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.order.ObjTicketId
import org.com.hcmurs.repositories.apis.order.OrderDaysRepository
import org.com.hcmurs.repositories.apis.order.OrderTicketDaysRequest
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import org.com.hcmurs.repositories.apis.ticket.TicketType
import javax.inject.Inject


data class OrderInfoUiState(
    val ticketType: TicketType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingOrder: Boolean = false,
    val orderCreationSuccess: Boolean = false,
    val orderCreationMessage: String? = null
)


@HiltViewModel
class OrderInfoViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val orderDaysRepository: OrderDaysRepository,
    savedStateHandle: SavedStateHandle
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
                paymentMethodId = paymentMethodId
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
    fun clearOrderCreationStatus() {
        _uiState.update { it.copy(orderCreationSuccess = false, orderCreationMessage = null) }
    }
}
