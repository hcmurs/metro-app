package org.com.hcmurs.repositories

import android.content.SharedPreferences
import org.com.hcmurs.security.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesTokenProvider @Inject constructor(
    private val prefs: SharedPreferences
) : TokenProvider {

    companion object {
        private const val TOKEN_KEY = "jwt_token"
    }

    override fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    override fun getToken(): String? = prefs.getString(TOKEN_KEY, null)

    override fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}
