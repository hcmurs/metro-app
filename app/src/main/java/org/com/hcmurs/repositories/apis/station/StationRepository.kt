package org.com.hcmurs.repositories.apis.station

import org.com.hcmurs.Station
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationRepository @Inject constructor(
    private val stationApi: StationApi
) {
    suspend fun getStations(): Result<List<Station>> {
        return try {
            val response = stationApi.getStations()
            if (response.isSuccessful && response.body() != null) {
                // Filter for stations with status "open"
                val openStations = response.body()!!.data.filter { it.status == "open" }
                Result.success(openStations)
            } else {
                Result.failure(RuntimeException("Failed to fetch stations: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
