/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.constant

enum class UserRole {
    USER,
    STAFF,
    ADMIN,
    GUEST,
    ;

    companion object {
        fun fromString(role: String): UserRole = when (role.lowercase()) {
            "user" -> USER
            "staff" -> STAFF
            "admin" -> ADMIN
            "guest" -> GUEST
            else -> throw IllegalArgumentException("Unknown role: $role")
        }
    }
}
