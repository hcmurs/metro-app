/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories

interface Store {
    fun getValue(key: String): String

    fun setValue(
        key: String,
        value: String,
    )
}
