/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
 */
package org.com.hcmurs

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject
import okhttp3.OkHttpClient
import okio.Path.Companion.toPath
import org.com.hcmurs.utils.LanguageManager

@HiltAndroidApp
class MainApplication : Application() {
    @Inject
    lateinit var okHttpClient: OkHttpClient

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate() {
        super.onCreate()

        val currentLang = LanguageManager.getLocale(this)
        LanguageManager.setLocale(this, currentLang)

        // Method 1: Using setSafe (recommended)
        SingletonImageLoader.setSafe {
            ImageLoader
                .Builder(this@MainApplication)
                .components {
                    add(OkHttpNetworkFetcherFactory(okHttpClient))
                }.memoryCache {
                    MemoryCache
                        .Builder()
                        .maxSizePercent(this@MainApplication, 0.25)
                        .build()
                }.diskCache {
                    DiskCache
                        .Builder()
                        .directory(
                            this@MainApplication
                                .cacheDir
                                .resolve("image_cache")
                                .absolutePath
                                .toPath(),
                        ).maxSizeBytes(512L * 1024 * 1024) // 512MB
                        .build()
                }.crossfade(true)
                .build()
        }
    }

    override fun attachBaseContext(base: Context?) {
        if (base != null) {
            val languageCode = LanguageManager.getLocale(base)
            val locale = Locale(languageCode)
            val config = Configuration(base.resources.configuration)
            config.setLocale(locale)
            val context = base.createConfigurationContext(config)
            super.attachBaseContext(context)
        } else {
            super.attachBaseContext(base)
        }
    }
}
