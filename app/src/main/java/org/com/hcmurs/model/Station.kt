package org.com.hcmurs.model

import org.osmdroid.util.GeoPoint

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
    val location: GeoPoint
        get() = GeoPoint(latitude, longitude)

    val isTerminal: Boolean
        get() = sequenceOrder == 1 || status.lowercase() == "terminal"
}
