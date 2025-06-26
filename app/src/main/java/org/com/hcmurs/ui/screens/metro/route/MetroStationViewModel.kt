package org.com.hcmurs.ui.screens.metro.route

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.com.hcmurs.model.Station
import org.com.hcmurs.repositories.apis.station.MetroStationRepository
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class MetroStationViewModel @Inject constructor(
    private val repository: MetroStationRepository
) : ViewModel() {

    private val _stations = MutableStateFlow<List<Station>>(emptyList())
    val stations = _stations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Computed state for map points
    val stationGeoPoints = stations.map { stations ->
        stations.map { GeoPoint(it.latitude, it.longitude) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        fetchStations()
    }

    fun fetchStations() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getStations()
                _stations.value = result
                Log.d("MetroStationViewModel", "Stations loaded: ${result.size}")
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("MetroStationViewModel", "Error loading stations", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
