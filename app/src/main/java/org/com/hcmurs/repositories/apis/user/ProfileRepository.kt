package org.com.hcmurs.repositories.apis.user

import org.com.hcmurs.model.UserProfile
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun getProfiles(): List<UserProfile> {
        val response = profileApi.getProfiles()
        if (!response.isSuccessful) {
            throw Exception("Error: ${response.code()} ${response.message()}")
        }
        return response.body() ?: emptyList()
    }
}