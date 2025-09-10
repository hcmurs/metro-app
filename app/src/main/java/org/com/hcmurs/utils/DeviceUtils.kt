/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceUtils
@Inject
constructor(
    private val context: Context,
) {
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String = try {
        // Try to get Android ID first
        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        if (androidId != null && androidId != "9774d56d682e549c") {
            androidId
        } else {
            // Fallback to a generated UUID stored in SharedPreferences
            getOrCreateDeviceId()
        }
    } catch (e: Exception) {
        getOrCreateDeviceId()
    }

    private fun getOrCreateDeviceId(): String {
        val sharedPrefs = context.getSharedPreferences("device_prefs", Context.MODE_PRIVATE)
        val existingId = sharedPrefs.getString("device_id", null)

        return if (existingId != null) {
            existingId
        } else {
            val newId = UUID.randomUUID().toString()
            sharedPrefs.edit().putString("device_id", newId).apply()
            newId
        }
    }

    fun getDeviceName(): String = "${Build.MANUFACTURER} ${Build.MODEL}".trim()

    fun getPlatform(): String = "ANDROID"

    companion object {
        val isEmulator: Boolean
            get() = Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("Emulator")
    }
}
