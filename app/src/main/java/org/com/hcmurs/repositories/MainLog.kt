/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs.repositories

interface MainLog {
    fun i(
        tag: String,
        msg: String,
    )

    fun d(
        tag: String,
        msg: String,
    )

    fun e(
        tag: String,
        msg: String,
    )
}
