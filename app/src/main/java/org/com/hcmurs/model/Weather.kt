/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.model

data class HourlyWeatherResponse(
    val hourly: HourlyWeatherData,
)

data class HourlyWeatherData(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val wind_speed_10m: List<Double>,
)
