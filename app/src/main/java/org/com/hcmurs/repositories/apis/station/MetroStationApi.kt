package org.com.hcmurs.repositories.apis.station

import org.com.hcmurs.dto.MyApiResponse
import org.com.hcmurs.model.Station
import retrofit2.Response
import retrofit2.http.GET

interface MetroStationApi {
    @GET("/api/stations")
    suspend fun getStations(): Response<MyApiResponse<List<Station>>>
}
