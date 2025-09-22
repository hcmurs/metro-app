/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.station

import retrofit2.Response
import retrofit2.http.GET

// https://run.mocky.io/v3/3beaec3a-9775-4baf-a424-bfd15cfbb320
interface BusStationApi {
    @GET("api/stations/static-bus-stations")
    suspend fun getBusStations(): Response<List<BusStation>>
}

data class BusStation(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isActive: Boolean,
    val address: String,
    val code: String,
)
