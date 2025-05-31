package org.com.hcmurs

import android.app.Application
import coil3.ComponentRegistry
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp
import okio.Path.Companion.toPath

@HiltAndroidApp
class MainApplication : Application() {
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate() {
        super.onCreate()

        // Method 1: Using setSafe (recommended)
        SingletonImageLoader.setSafe {
            ImageLoader.Builder(this@MainApplication)
                .components {
                    // Add any custom components if needed
                }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(this@MainApplication, 0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(this@MainApplication.cacheDir.resolve("image_cache").absolutePath.toPath())
                        .maxSizeBytes(512L * 1024 * 1024) // 512MB
                        .build()
                }
                .crossfade(true)
                .build()
        }
    }
}