package org.com.hcmurs.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.JavaNetCookieJar
import org.com.hcmurs.repositories.BusStationApi
import org.com.hcmurs.repositories.BusStationRepository
import org.com.hcmurs.repositories.ProfileApi
import org.com.hcmurs.repositories.SharedPreferencesTokenProvider
import org.com.hcmurs.security.TokenProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val BASE_URL = "http://10.0.2.2:4003/"
    private val MOCKY_BASE_URL = "https://run.mocky.io/"

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: TokenProvider): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val token = tokenProvider.getToken()

            val requestBuilder: Request.Builder = original.newBuilder()
            token?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideTokenProvider(@ApplicationContext context: Context): TokenProvider {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return SharedPreferencesTokenProvider(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager {
        return CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor, cookieManager: CookieManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .cookieJar(JavaNetCookieJar(cookieManager))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }

    //mock bus station api
    @Provides
    @Singleton
    fun provideBusStationApi(
        @Named("mockyRetrofit") retrofit: Retrofit
    ): BusStationApi {
        return retrofit.create(BusStationApi::class.java)
    }

    @Provides
    @Named("mockyRetrofit")
    @Singleton
    fun provideMockyRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MOCKY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBusStationRepository(api: BusStationApi): BusStationRepository {
        return BusStationRepository(api)
    }
}