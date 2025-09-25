/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.model

import com.google.gson.annotations.SerializedName

data class UserDeviceTokenRequest(
    @SerializedName("email") val email: String,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("deviceName") val deviceName: String,
    @SerializedName("platform") val platform: String,
)

data class UserDeviceTokenResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("deviceName") val deviceName: String,
    @SerializedName("platform") val platform: String,
)
