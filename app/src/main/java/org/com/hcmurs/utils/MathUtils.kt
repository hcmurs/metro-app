package org.com.hcmurs.utils

import org.com.hcmurs.model.BusStop
import org.osmdroid.util.GeoPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun distanceBetween(p1: GeoPoint, p2: GeoPoint): Double {
    val R = 6371000.0 // Earth radius in meters
    val dLat = Math.toRadians(p2.latitude - p1.latitude)
    val dLon = Math.toRadians(p2.longitude - p1.longitude)

    val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(p1.latitude)) *
            cos(Math.toRadians(p2.latitude)) * sin(dLon / 2).pow(2.0)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c
}

fun getNearbyBusStops(
    station: GeoPoint,
    allBusStops: List<BusStop>,
    radiusMeters: Double = 1000.0
): List<BusStop> {
    return allBusStops.filter {
        val busGeo = GeoPoint(it.latitude, it.longitude)
        distanceBetween(station, busGeo) <= radiusMeters
    }
}

fun getNearbyBusStopsForStations(
    stations: List<GeoPoint>,
    allBusStops: List<BusStop>,
    radiusMeters: Double = 2000.0
): List<BusStop> {
    return allBusStops.filter { busStop ->
        val busGeo = GeoPoint(busStop.latitude, busStop.longitude)
        stations.any { station ->
            distanceBetween(station, busGeo) <= radiusMeters
        }
    }
}