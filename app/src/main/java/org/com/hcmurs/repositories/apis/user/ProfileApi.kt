package org.com.hcmurs.repositories.apis.user

import org.com.hcmurs.model.UserProfile
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("profiles")
    suspend fun getProfiles(): Response<List<UserProfile>>
}