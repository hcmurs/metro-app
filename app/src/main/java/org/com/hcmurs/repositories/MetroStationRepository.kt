package org.com.hcmurs.repositories

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
                        id = station.id ?: 0,
                        name = station.name,
                        latitude = station.latitude,
                        longitude = station.longitude
                    )
                }
            } else {
                throw Exception("Failed to fetch metro stations: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }
}