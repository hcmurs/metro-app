/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories.apis.user

import javax.inject.Inject
import org.com.hcmurs.model.UserProfile

class ProfileRepository
@Inject
constructor(
    private val profileApi: ProfileApi,
) {
    suspend fun getProfiles(): List<UserProfile> {
        val response = profileApi.getProfiles()
        if (!response.isSuccessful) {
            throw Exception("Error: ${response.code()} ${response.message()}")
        }
        return response.body() ?: emptyList()
    }
}
