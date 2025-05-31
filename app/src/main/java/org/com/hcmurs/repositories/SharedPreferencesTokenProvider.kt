package org.com.hcmurs.repositories

import android.content.SharedPreferences
import org.com.hcmurs.constant.AuthConstants
import org.com.hcmurs.security.TokenProvider
import javax.inject.Inject

class SharedPreferencesTokenProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenProvider {
    override fun getToken(): String? {
        return sharedPreferences.getString(AuthConstants.TOKEN_KEY, null)
    }
}
