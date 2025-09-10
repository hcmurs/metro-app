/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.security

interface TokenProvider {
    fun getToken(): String?

    fun saveToken(token: String)

    fun clearToken()
}
