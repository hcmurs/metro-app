package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import org.com.hcmurs.repositories.apis.ticket.FareMatrixRepository
import javax.inject.Inject


data class FareMatrixUiState(
    val fareMatrices: List<FareMatrix> = emptyList(),
    val calculatedFare: FareMatrixResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreatingOrder: Boolean = false,
    val createOrderResponse: CreateOrderResponse? = null,
    val createOrderError: String? = null
)

@HiltViewModel
class FareMatrixViewModel @Inject constructor(
    private val fareMatrixRepository: FareMatrixRepository,
    private val orderRepository: OrderRepository
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


    fun clearCreateOrderStatus() {
        _uiState.update { it.copy(createOrderResponse = null, createOrderError = null) }
    }


}