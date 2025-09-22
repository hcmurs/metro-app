/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.com.hcmurs.repositories.apis.station.MetroStationRepository
import org.com.hcmurs.ui.screens.metro.route.MetroStationViewModel

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideMetroStationViewModel(repository: MetroStationRepository): MetroStationViewModel = MetroStationViewModel(repository)
}
