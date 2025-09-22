/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.utils

import android.util.Base64
import java.lang.Exception
import org.json.JSONObject

object JwtUtils {
    fun isTokenExpired(token: String?): Boolean {
        if (token.isNullOrEmpty()) return true
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            val exp = json.getLong("exp")
            val currentTime = System.currentTimeMillis() / 1000 // seconds
            currentTime >= exp
        } catch (e: Exception) {
            true // Bất kỳ lỗi nào cũng xem như hết hạn
        }
    }
}
