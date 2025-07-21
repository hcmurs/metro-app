package org.com.hcmurs.repositories.apis.station

import android.util.Log
import org.com.hcmurs.RouteResponse
import org.com.hcmurs.Station
import org.com.hcmurs.StationRouteResponse
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

    suspend fun getRoutes(): Result<List<RouteResponse>> {
        return try {
            val apiResponse = stationApi.getRoutes()

            if (apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Log.w("StationRepository", "getRoutes returned null data. Message: ${apiResponse.message}")
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.e("StationRepository", "getRoutes exception", e)
            Result.failure(e)
        }
    }

    /**
     * MỚI: Lấy danh sách các trạm thuộc một tuyến đường cụ thể bằng routeId.
     */
    suspend fun getStationsByRouteId(routeId: Long): Result<List<StationRouteResponse>> {
        return try {
            val apiResponse = stationApi.getStationsByRouteId(routeId) //

            if (apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Log.w("StationRepository", "getStationsByRouteId for route $routeId returned null data. Message: ${apiResponse.message}")
                Result.success(emptyList())
            }
        } catch (e: Exception) {
            Log.e("StationRepository", "getStationsByRouteId exception for route $routeId", e)
            Result.failure(e)
        }
    }
}
