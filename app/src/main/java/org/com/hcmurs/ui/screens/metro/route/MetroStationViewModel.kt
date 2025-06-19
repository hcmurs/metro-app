package org.com.hcmurs.ui.screens.metro.route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.com.hcmurs.model.BusStop
import org.com.hcmurs.repositories.MetroStationRepository
import org.com.hcmurs.repositories.apis.MetroStation
import javax.inject.Inject

@HiltViewModel
class MetroStationViewModel @Inject constructor(
    private val repository: MetroStationRepository
) : ViewModel() {

    private val _metroStations = MutableStateFlow<List<MetroStation>>(emptyList())
    val metroStations: StateFlow<List<MetroStation>> = _metroStations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getMetroStations() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _metroStations.value = repository.getMetroStations()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}