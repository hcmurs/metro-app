/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
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
