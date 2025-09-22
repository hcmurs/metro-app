/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
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
    val routeId: Int,
) {
    val location: GeoPoint
        get() = GeoPoint(latitude, longitude)

    val isTerminal: Boolean
        get() = sequenceOrder == 1 || status.lowercase() == "terminal"
}
