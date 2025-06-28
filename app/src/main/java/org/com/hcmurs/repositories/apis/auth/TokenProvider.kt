package org.com.hcmurs.repositories.apis.auth

interface TokenProvider {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}