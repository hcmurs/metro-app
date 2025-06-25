package org.com.hcmurs.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {
    private const val LANGUAGE_KEY = "selected_language"
    private const val DEFAULT_LANGUAGE = "vi" // Vietnamese as default

    fun setLocale(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString(LANGUAGE_KEY, languageCode).apply()
        
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getLocale(context: Context): String {
        val prefs = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        return prefs.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun getCurrentLanguageName(context: Context): String {
        return when (getLocale(context)) {
            "vi" -> "Vietnamese"
            "en" -> "English"
            else -> "Vietnamese"
        }
    }

    fun getAvailableLanguages(): List<Pair<String, String>> {
        return listOf(
            "vi" to "Vietnamese",
            "en" to "English"
        )
    }
}