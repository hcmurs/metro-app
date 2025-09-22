/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories

import android.util.Log
import javax.inject.Inject

class MainLogImpl
@Inject
constructor() : MainLog {
    override fun i(
        tag: String,
        msg: String,
    ) {
        Log.i(tag, msg)
    }

    override fun d(
        tag: String,
        msg: String,
    ) {
        Log.d(tag, msg)
    }

    override fun e(
        tag: String,
        msg: String,
    ) {
        Log.e(tag, msg)
    }
}
