/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.com.hcmurs.repositories.Api
import org.com.hcmurs.repositories.ApiImpl
import org.com.hcmurs.repositories.MainLog
import org.com.hcmurs.repositories.MainLogImpl
import org.com.hcmurs.repositories.Store
import org.com.hcmurs.repositories.StoreImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds // link to a specific implementation of the interface
    @Singleton
    abstract fun bindMainLog(
        log: MainLogImpl,
    ): MainLog

    @Binds
    @Singleton
    abstract fun bindApi(api: ApiImpl): Api

    @Binds
    @Singleton
    abstract fun bindStore(store: StoreImpl): Store
}
