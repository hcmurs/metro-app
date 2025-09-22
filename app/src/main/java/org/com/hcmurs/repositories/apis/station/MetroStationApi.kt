/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.station

import org.com.hcmurs.dto.MyApiResponse
import org.com.hcmurs.model.Station
import retrofit2.Response
import retrofit2.http.GET

interface MetroStationApi {
    @GET("/api/stations")
    suspend fun getStations(): Response<MyApiResponse<List<Station>>>
}
