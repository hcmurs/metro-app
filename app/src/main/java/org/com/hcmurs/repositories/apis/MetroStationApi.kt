package org.com.hcmurs.repositories.apis

import org.osmdroid.util.GeoPoint
import retrofit2.Response
import retrofit2.http.GET

interface MetroStationApi {
    @GET("v3/bbbbdafe-d253-4291-8029-5a784262e7b1")
    suspend fun getMetroStations(): Response<List<MetroStation>>
}

data class MetroStation(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val routeIndex: Int, // Position in the route (0-10)
    val isTerminal: Boolean = false, // Whether this is a terminal station (start/end)
) {
    // Convenience property to get GeoPoint directly
    val location: GeoPoint
        get() = GeoPoint(latitude, longitude)

    // Static helper to identify stations
    companion object {
        const val START_STATION_ID = 1 // First station ID
        const val END_STATION_ID = 11 // Last station ID

        // Create a list of stations from the fixed GeoPoint list
        fun createFromGeoPoints(points: List<GeoPoint>): List<MetroStation> {
            val stationNames = listOf(
                "Bến Thành", "Nhà hát TP", "Ba Son", "Văn Thánh",
                "Tân Cảng", "Thảo Điền", "An Phú", "Rạch Chiếc",
                "Phước Long", "Bình Thái", "Thủ Đức"
            )

            return points.mapIndexed { index, point ->
                MetroStation(
                    id = index + 1,
                    name = stationNames[index],
                    latitude = point.latitude,
                    longitude = point.longitude,
                    routeIndex = index,
                    isTerminal = index == 0 || index == points.size - 1
                )
            }
        }
    }
}

