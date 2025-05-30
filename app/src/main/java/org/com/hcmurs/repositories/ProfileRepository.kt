package org.com.hcmurs.repositories

import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun getProfiles() = profileApi.getProfiles()
}