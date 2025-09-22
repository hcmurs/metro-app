/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.station

import javax.inject.Inject
import org.com.hcmurs.model.BusStop

class BusStationRepository
@Inject
constructor(
    private val api: BusStationApi,
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
                        longitude = station.longitude,
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
