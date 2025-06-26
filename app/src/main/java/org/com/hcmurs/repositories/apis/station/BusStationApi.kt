package org.com.hcmurs.repositories.apis.station

import retrofit2.Response
import retrofit2.http.GET

//https://run.mocky.io/v3/3beaec3a-9775-4baf-a424-bfd15cfbb320
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
    val code: String
)