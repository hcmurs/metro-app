package org.com.hcmurs.repositories.apis.station

import android.util.Log
import org.com.hcmurs.model.Station
import javax.inject.Inject

class MetroStationRepository @Inject constructor(
    private val api: MetroStationApi
) {
    suspend fun getStations(): List<Station> {
        return try {
            val response = api.getStations()
            if (response.isSuccessful) {
                Log.d("MetroStationRepository", "Response: ${response.body()?.data?.size}")
                response.body()?.data?.sortedBy { it.sequenceOrder } ?: emptyList()
            } else {
                throw Exception("API error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }
}
