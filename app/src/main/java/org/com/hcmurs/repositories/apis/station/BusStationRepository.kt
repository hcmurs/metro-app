package org.com.hcmurs.repositories.apis.station

import org.com.hcmurs.model.BusStop
import javax.inject.Inject

class BusStationRepository @Inject constructor(
    private val api: BusStationApi
) {
    suspend fun getBusStations(): List<BusStop> {
        try {
            val response = api.getBusStations()
            return if (response.isSuccessful && response.body() != null) {
                response.body()!!.map { station ->
                    BusStop(
                        id = station.id.toIntOrNull() ?: 0,
                        name = station.name,
                        latitude = station.latitude,
                        longitude = station.longitude
                    )
                }
            } else {
                throw Exception("Failed to fetch bus stations: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }
}