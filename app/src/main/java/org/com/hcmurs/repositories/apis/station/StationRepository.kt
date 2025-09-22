/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.station

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton
import org.com.hcmurs.RouteResponse
import org.com.hcmurs.Station
import org.com.hcmurs.StationRouteResponse

@Singleton
class StationRepository @Inject constructor(
    private val stationApi: StationApi,
) {
    suspend fun getStations(): Result<List<Station>> = try {
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

    suspend fun getRoutes(): Result<List<RouteResponse>> = try {
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

    suspend fun getStationsByRouteId(routeId: Long): Result<List<StationRouteResponse>> = try {
        val apiResponse = stationApi.getStationsByRouteId(routeId)

        if (apiResponse.data != null) {
            val activeStations = apiResponse.data.filter { it.status.equals("ACTIVE", ignoreCase = true) }
            Result.success(activeStations)
        } else {
            Log.w("StationRepository", "getStationsByRouteId for route $routeId returned null data. Message: ${apiResponse.message}")
            Result.success(emptyList())
        }
    } catch (e: Exception) {
        Log.e("StationRepository", "getStationsByRouteId exception for route $routeId", e)
        Result.failure(e)
    }
}
