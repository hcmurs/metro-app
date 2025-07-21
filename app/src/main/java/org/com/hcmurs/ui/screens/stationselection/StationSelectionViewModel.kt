package org.com.hcmurs.ui.screens.stationselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.com.hcmurs.RouteResponse
import org.com.hcmurs.Station
import org.com.hcmurs.repositories.apis.station.StationRepository
import javax.inject.Inject


data class StationSelectionUiState(
    val routes: List<RouteResponse> = emptyList(),
    val stations: List<Station> = emptyList(),
    val selectedRoute: RouteResponse? = null,
    val isLoadingRoutes: Boolean = false,
    val isLoadingStations: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class StationSelectionViewModel @Inject constructor(
    private val stationRepository: StationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StationSelectionUiState())
    val uiState: StateFlow<StationSelectionUiState> = _uiState.asStateFlow()


    init {
        fetchRoutes()
    }


    private fun fetchRoutes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingRoutes = true, errorMessage = null) }
            stationRepository.getRoutes()
                .onSuccess { routes ->
                    _uiState.update { it.copy(routes = routes, isLoadingRoutes = false) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            errorMessage = throwable.localizedMessage ?: "Unknown error loading routes",
                            isLoadingRoutes = false
                        )
                    }
                }
        }
    }
    fun onRouteSelected(route: RouteResponse) {
        if (_uiState.value.selectedRoute?.routeId == route.routeId) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedRoute = route,
                    isLoadingStations = true,
                    stations = emptyList(),
                    errorMessage = null
                )
            }

            stationRepository.getStationsByRouteId(route.routeId.toLong())
                .onSuccess { stationRoutes ->
                    val stations = stationRoutes.map { it.stationsResponse }
                    _uiState.update {
                        it.copy(stations = stations, isLoadingStations = false)
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            errorMessage = throwable.localizedMessage ?: "Unknown error loading stations",
                            isLoadingStations = false
                        )
                    }
                }
        }
    }
}