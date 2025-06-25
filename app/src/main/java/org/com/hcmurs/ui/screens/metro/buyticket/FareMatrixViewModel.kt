package org.com.hcmurs.ui.screens.metro.buyticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.FareMatrix
import org.com.hcmurs.repositories.FareMatrixRepository
import javax.inject.Inject


data class FareMatrixUiState(
    val fareMatrices: List<FareMatrix> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FareMatrixViewModel @Inject constructor(
    private val fareMatrixRepository: FareMatrixRepository
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
}