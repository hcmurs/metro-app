/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StoreImpl
@Inject
constructor(
    @ApplicationContext context: Context,
    private val sharedPreferences: SharedPreferences,
) : Store {
    override fun getValue(key: String): String = sharedPreferences.getString(key, "") ?: ""

    override fun setValue(
        key: String,
        value: String,
    ) {
        sharedPreferences.edit { putString(key, value) }
    }
}
