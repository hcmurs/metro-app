package org.com.hcmurs.repositories.apis.weather

import org.com.hcmurs.model.HourlyWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,wind_speed_10m"
    ): HourlyWeatherResponse
}