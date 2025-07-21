package org.com.hcmurs

import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime

data class FareMatricesResponse(
    val status: Int,
    val message: String,
    val data: List<FareMatrix>
)

data class FareMatrixResponse(
    val status: Int,
    val message: String,
    val data: FareMatrix
)

data class FareMatrix(
    val fareMatrixId: Int,
    val name: String,
    val price: Int,
    val startStationId: Int,
    val endStationId: Int,
    val createdAt: String,
    val updatedAt: String
)


data class StationResponse(
    val status: Int,
    val message: String,
    val data: List<Station>
)

data class Station(
    val stationId: Int,
    val stationCode: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
) {
    fun toGeoPoint(): GeoPoint {
        return GeoPoint(latitude, longitude)
    }
}
    data class RouteResponse(
        val routeId: Int,
        val routeName: String,
        val routeCode: String,
        val startStationId: Int,
        val distanceInKm: Double,

    )

    data class StationRouteResponse(
        val id: Int,
        val routeId: Int,
        val sequenceOrder: Int,
        val status: String,
        val createdAt: String,
        val updatedAt: String,

        @SerializedName("stationsResponse")
        val stationsResponse: Station
    )

