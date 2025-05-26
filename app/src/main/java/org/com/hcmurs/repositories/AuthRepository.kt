package org.com.hcmurs.repositories

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun storeToken(token: String) = withContext(Dispatchers.IO) {
        sharedPrefs.edit().putString("access_token", token).apply()
    }

    suspend fun getStoredToken(): String? = withContext(Dispatchers.IO) {
        sharedPrefs.getString("access_token", null)
    }

    suspend fun clearToken() = withContext(Dispatchers.IO) {
        sharedPrefs.edit().remove("access_token").apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun isTokenValid(token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // Basic JWT token validation (you might want to use a proper JWT library)
            val parts = token.split(".")
            if (parts.size != 3) return@withContext false

            val payload = String(Base64.getDecoder().decode(parts[1]))
            val json = JSONObject(payload)
            val exp = json.getLong("exp")
            val currentTime = System.currentTimeMillis() / 1000

            exp > currentTime
        } catch (e: Exception) {
            false
        }
    }
}