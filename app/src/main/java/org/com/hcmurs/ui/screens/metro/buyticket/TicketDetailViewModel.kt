package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import javax.inject.Inject



// ViewModel for TicketDetailScreen
@HiltViewModel
class TicketDetailViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    savedStateHandle: SavedStateHandle // To access navigation arguments
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketDetailUiState())
    val uiState: StateFlow<TicketDetailUiState> = _uiState

    // Retrieve ticketId from navigation arguments
    private val ticketId: Int? = savedStateHandle.get<String>("ticketId")?.toIntOrNull()

    init {
        fetchTicketDetail()
    }

    private fun fetchTicketDetail() {
        if (ticketId == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "Ticket ID is missing.")
            return
        }




        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = ticketRepository.getTicketTypes() // Fetch all types
            result.onSuccess { tickets ->
                // Find the specific ticket by ID
                val foundTicket = tickets.find { it.id == ticketId }
                if (foundTicket != null) {
                    _uiState.value = _uiState.value.copy(ticketDetail = foundTicket)
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = "Ticket not found.")
                }
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(errorMessage = throwable.localizedMessage ?: "Unknown error")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}