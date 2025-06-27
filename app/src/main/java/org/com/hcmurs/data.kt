package org.com.hcmurs

import org.osmdroid.util.GeoPoint

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
// Represents a single fare matrix entry
data class FareMatrix(
    val fareMatrixId: Int,
    val name: String,
    val price: Int,
    val startStationId: Int,
    val endStationId: Int,
    val createdAt: String,
    val updatedAt: String
)


// Matches the overall API response structure for stations
data class StationResponse(
    val status: Int,
    val message: String,
    val data: List<Station>
)

// Represents a single station entry from the API
data class Station(
    val stationId: Int,
    val stationCode: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val sequenceOrder: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val routeId: Int
) {
    // Helper to convert to GeoPoint if needed for map integration
    fun toGeoPoint(): GeoPoint {
        return GeoPoint(latitude, longitude)
    }
}
