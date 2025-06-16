package org.com.hcmurs.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.openid.appauth.AuthorizationService
import org.com.hcmurs.repositories.AuthApiService
import org.com.hcmurs.repositories.AuthRepository
import org.com.hcmurs.repositories.IAuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideAuthorizationService(@ApplicationContext context: Context): AuthorizationService {
        return AuthorizationService(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        authorizationService: AuthorizationService,
        @ApplicationContext context: Context
    ): IAuthRepository {
        return AuthRepository(authApiService, authorizationService,context)
    }

}