package org.com.hcmurs.repositories

import org.com.hcmurs.model.station
import org.com.hcmurs.repositories.apis.MetroStation
import org.com.hcmurs.repositories.apis.MetroStationApi
import javax.inject.Inject

class MetroStationRepository @Inject constructor(
    private val api: MetroStationApi
) {
    suspend fun getMetroStations(): List<MetroStation> {
        try {
            val response = api.getMetroStations()
            return if (response.isSuccessful && response.body() != null) {
                response.body()!!.map { station ->
                    MetroStation(
                        id = station.id,
                        name = station.name,
                        latitude = station.latitude,
                        longitude = station.longitude,
                        routeIndex = station.id - 1, // Assume ID starts from 1
                        isTerminal = station.id == 1 || station.id == 11
                    )
                }
            } else {
                // Fallback to creating from hardcoded GeoPoints
                MetroStation.createFromGeoPoints(station)
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }
}