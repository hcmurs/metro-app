/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories.apis.station

import android.util.Log
import javax.inject.Inject
import org.com.hcmurs.model.Station

class MetroStationRepository
@Inject
constructor(
    private val api: MetroStationApi,
) {
    suspend fun getStations(): List<Station> = try {
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
