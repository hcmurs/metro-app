package org.com.hcmurs.repositories.apis.weather

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

data class WeatherResult(
    val temperature: Double,
    val windSpeed: Double
)

class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTemperature(lat: Double, lon: Double): Double? {
        val response = api.getWeather(lat, lon)
        val now = LocalDateTime.now(ZoneOffset.UTC).toString().substring(0, 13)
        val index = response.hourly.time.indexOfFirst { it.startsWith(now) }
        return response.hourly.temperature_2m.getOrNull(index)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResult? {
        val response = api.getWeather(lat, lon)
        val now = LocalDateTime.now(ZoneOffset.UTC).toString().substring(0, 13)
        val index = response.hourly.time.indexOfFirst { it.startsWith(now) }

        val temp = response.hourly.temperature_2m.getOrNull(index)
        val wind = response.hourly.wind_speed_10m.getOrNull(index)

        return if (temp != null && wind != null) WeatherResult(temp, wind) else null
    }
}