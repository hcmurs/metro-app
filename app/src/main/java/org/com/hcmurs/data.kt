/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs

import com.google.gson.annotations.SerializedName
import org.osmdroid.util.GeoPoint

data class FareMatricesResponse(
    val status: Int,
    val message: String,
    val data: List<FareMatrix>,
)

data class FareMatrixResponse(
    val status: Int,
    val message: String,
    val data: FareMatrix,
)

data class FareMatrix(
    val fareMatrixId: Int,
    val name: String,
    val price: Int,
    val startStationId: Int,
    val endStationId: Int,
    val createdAt: String,
    val updatedAt: String,
)

data class StationResponse(
    val status: Int,
    val message: String,
    val data: List<Station>,
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
    fun toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
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
    val stationsResponse: Station,
)
