/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.weather

import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

data class WeatherResult(
    val temperature: Double,
    val windSpeed: Double,
)

class WeatherRepository
@Inject
constructor(
    private val api: WeatherApi,
) {

    suspend fun getTemperature(
        lat: Double,
        lon: Double,
    ): Double? {
        val response = api.getWeather(lat, lon)
        val now = LocalDateTime.now(ZoneOffset.UTC).toString().substring(0, 13)
        val index = response.hourly.time.indexOfFirst { it.startsWith(now) }
        return response.hourly.temperature_2m.getOrNull(index)
    }

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
    ): WeatherResult? {
        val response = api.getWeather(lat, lon)
        val now = LocalDateTime.now(ZoneOffset.UTC).toString().substring(0, 13)
        val index = response.hourly.time.indexOfFirst { it.startsWith(now) }

        val temp = response.hourly.temperature_2m.getOrNull(index)
        val wind = response.hourly.wind_speed_10m.getOrNull(index)

        return if (temp != null && wind != null) WeatherResult(temp, wind) else null
    }
}
