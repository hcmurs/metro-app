package org.com.hcmurs.ui.screens.metro.myticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.repositories.apis.order.OrderRepository
import org.com.hcmurs.repositories.apis.order.OrderWithTicketDetails
import javax.inject.Inject


data class MyTicketUiState(
    val orders: List<OrderWithTicketDetails> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)



@HiltViewModel
class MyTicketViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyTicketUiState())
    val uiState: StateFlow<MyTicketUiState> = _uiState.asStateFlow()

    init {
        fetchUserOrders()
    }

    fun fetchUserOrders() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = orderRepository.getUserOrdersWithDetails()
            result.onSuccess { response ->
                if (response.status == 200 || response.status == 0) {
                    _uiState.update { it.copy(orders = response.data ?: emptyList(), isLoading = false) }
                } else {
                    _uiState.update { it.copy(errorMessage = response.message, isLoading = false) }
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.localizedMessage ?: "An error occurred", isLoading = false)
                }
            }
        }
    }
}