package org.com.hcmurs.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.com.hcmurs.repositories.apis.station.MetroStationRepository
import org.com.hcmurs.ui.screens.metro.route.MetroStationViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideMetroStationViewModel(
        repository: MetroStationRepository
    ): MetroStationViewModel = MetroStationViewModel(repository)

}