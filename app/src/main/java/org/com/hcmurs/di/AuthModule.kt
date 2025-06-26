//package org.com.hcmurs.di
//
//import android.content.Context
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import org.com.hcmurs.oauth.OAuth2Service
//import org.com.hcmurs.repositories.AuthApi
//import org.com.hcmurs.repositories.apis.auth.AuthRepository
//import retrofit2.Retrofit
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class AuthModule {
//
//    @Provides
//    @Singleton
//    fun provideOAuth2Service(@ApplicationContext context: Context): OAuth2Service {
//        return OAuth2Service(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideAuthApi(retrofit: Retrofit): AuthApi {
//        return retrofit.create(AuthApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideAuthRepository(
//        @ApplicationContext context: Context,
//        authApi: AuthApi,
//        oAuth2Service: OAuth2Service
//    ): AuthRepository {
//        return AuthRepository(context, authApi, oAuth2Service)
//    }
//}