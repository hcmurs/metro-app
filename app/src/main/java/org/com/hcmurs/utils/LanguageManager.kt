/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.utils

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LanguageManager {
    private const val LANGUAGE_KEY = "selected_language"
    private const val DEFAULT_LANGUAGE = "vi" // Vietnamese as default

    private var currencyManager: CurrencyManager? = null

    fun setCurrencyManager(manager: CurrencyManager) {
        currencyManager = manager
    }

    fun setLocale(
        context: Context,
        languageCode: String,
    ) {
        val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        prefs.edit { putString(LANGUAGE_KEY, languageCode) }

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // Update configuration for current context
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Also update application context if available
        val appContext = context.applicationContext
        if (appContext != context) {
            appContext.resources.updateConfiguration(config, appContext.resources.displayMetrics)
        }

        // Update exchange rate when language changes
        currencyManager?.let { manager ->
            CoroutineScope(Dispatchers.IO).launch {
                manager.updateExchangeRate()
            }
        }
    }

    fun getLocale(context: Context): String {
        val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        return prefs.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun getCurrentLanguageName(context: Context): String = when (getLocale(context)) {
        "vi" -> "Vietnamese"
        "en" -> "English"
        else -> "Vietnamese"
    }

    fun getAvailableLanguages(): List<Pair<String, String>> = listOf(
        "vi" to "Vietnamese",
        "en" to "English",
    )
}
