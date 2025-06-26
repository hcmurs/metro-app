package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import org.com.hcmurs.repositories.apis.ticket.TicketType
import javax.inject.Inject


data class OrderInfoUiState(
    val ticketType: TicketType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class OrderInfoViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
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
}
