/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.user

import org.com.hcmurs.model.UserProfile
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("profiles")
    suspend fun getProfiles(): Response<List<UserProfile>>
}
