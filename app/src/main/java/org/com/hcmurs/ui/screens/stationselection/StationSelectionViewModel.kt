package org.com.hcmurs.ui.screens.stationselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.Station
import org.com.hcmurs.repositories.apis.station.StationRepository
import javax.inject.Inject


data class StationSelectionUiState(
    val stations: List<Station> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class StationSelectionViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StationSelectionUiState())
    val uiState: StateFlow<StationSelectionUiState> = _uiState


    init {
        fetchStations()
    }

    fun fetchStations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = stationRepository.getStations()
            result.onSuccess { stations ->
                _uiState.value = _uiState.value.copy(stations = stations)
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(errorMessage = throwable.localizedMessage ?: "Unknown error")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}