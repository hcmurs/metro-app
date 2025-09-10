/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories.apis

data class MyApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
)
