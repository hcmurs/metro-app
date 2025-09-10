/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.dto

class MyApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
) {
    override fun toString(): String = "MyApiResponse(status=$status, message='$message', data=$data)"
}
