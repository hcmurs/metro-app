/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.station

import org.com.hcmurs.RouteResponse
import org.com.hcmurs.StationResponse
import org.com.hcmurs.StationRouteResponse
import org.com.hcmurs.repositories.apis.request.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StationApi {
    @GET("api/stations")
    suspend fun getStations(): Response<StationResponse>

    @GET("api/routes")
    suspend fun getRoutes(): ApiResponse<List<RouteResponse>>

    @GET("api/station-routes/route/{routeId}")
    suspend fun getStationsByRouteId(
        @Path("routeId") routeId: Long,
    ): ApiResponse<List<StationRouteResponse>>
}
