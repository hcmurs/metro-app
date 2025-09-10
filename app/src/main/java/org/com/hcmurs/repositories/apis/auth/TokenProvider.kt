/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories.apis.auth

interface TokenProvider {
    fun saveToken(token: String)

    fun getToken(): String?

    fun clearToken()
}
