package org.com.hcmurs.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.com.hcmurs.repositories.BusStationRepository
import org.com.hcmurs.repositories.MetroStationRepository
import org.com.hcmurs.repositories.SharedPreferencesTokenProvider
import org.com.hcmurs.repositories.apis.AuthApi
import org.com.hcmurs.repositories.apis.BusStationApi
import org.com.hcmurs.repositories.apis.MetroStationApi
import org.com.hcmurs.repositories.apis.ProfileApi
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
    private val BASE_URL = "http://10.0.2.2:4006/"
    private val MOCKY_BASE_URL = "https://run.mocky.io/"
    private val BASE_BLOG = "http://10.0.2.2:4007/"

    @Provides
    @Singleton
    @Named("Auth")
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
    fun provideOkHttpClient(
        @Named("Auth")   authInterceptor: Interceptor,
        @Named("ApiKey") apiKeyInterceptor: Interceptor,
        cookieManager: CookieManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(apiKeyInterceptor)
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
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }


    @Provides
    @Singleton
    @Named("ApiKey")
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Content-Type", "application/json") // luôn set JSON
                .header(
                    "x-api-key",
                    "c761c9f0bb379612afbfd6ffeca90261db961bb93bce17728bc2a74430a66c0a"
                ) // API key của bạn
                .build()
            chain.proceed(request)
        }
    }

    //metro station api
    @Provides
    @Singleton
    fun provideMetroStationApi(
        @Named("mockyRetrofit") retrofit: Retrofit
    ): MetroStationApi {
        return retrofit.create(MetroStationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMetroStationRepository(api: MetroStationApi): MetroStationRepository {
        return MetroStationRepository(api)
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