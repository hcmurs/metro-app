package org.com.hcmurs.repositories.apis

import retrofit2.Response
import retrofit2.http.GET

interface MetroStationApi {
    @GET("v3/759a443f-4e9c-4825-b309-c4b837f2950c")
    suspend fun getMetroStations(): Response<List<MetroStation>>
}

data class MetroStation(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

