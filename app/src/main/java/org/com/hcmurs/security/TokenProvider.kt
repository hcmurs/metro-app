package org.com.hcmurs.security

interface TokenProvider {
    fun getToken(): String?
}
