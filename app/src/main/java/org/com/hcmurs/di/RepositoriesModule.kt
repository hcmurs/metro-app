package org.com.hcmurs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.com.hcmurs.repositories.Api
import org.com.hcmurs.repositories.ApiImpl
import org.com.hcmurs.repositories.MainLog
import org.com.hcmurs.repositories.MainLogImpl
import org.com.hcmurs.repositories.Store
import org.com.hcmurs.repositories.StoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds //link to a specific implementation of the interface
    @Singleton
    abstract fun bindMainLog(
        log: MainLogImpl
    ): MainLog

    @Binds
    @Singleton
    abstract fun bindApi(
        api: ApiImpl
    ): Api

    @Binds
    @Singleton
    abstract fun bindStore(store: StoreImpl): Store
}