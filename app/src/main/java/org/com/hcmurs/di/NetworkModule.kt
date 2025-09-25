/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.com.hcmurs.repositories.SharedPreferencesTokenProvider
import org.com.hcmurs.repositories.apis.auth.AuthApi
import org.com.hcmurs.repositories.apis.auth.AuthRepository
import org.com.hcmurs.repositories.apis.blog.BlogRepository
import org.com.hcmurs.repositories.apis.blog.PublicBlogApi
import org.com.hcmurs.repositories.apis.currency.CurrencyApi
import org.com.hcmurs.repositories.apis.feedback.FeedbackApi
import org.com.hcmurs.repositories.apis.feedback.FeedbackRepository
import org.com.hcmurs.repositories.apis.notification.NotificationApi
import org.com.hcmurs.repositories.apis.notification.NotificationRepository
import org.com.hcmurs.repositories.apis.order.OrderDaysApi
import org.com.hcmurs.repositories.apis.order.OrderDaysRepository
import org.com.hcmurs.repositories.apis.order.OrderSingleApi
import org.com.hcmurs.repositories.apis.payment.PaymentApi
import org.com.hcmurs.repositories.apis.payment.PaymentRepository
import org.com.hcmurs.repositories.apis.request.RequestApi
import org.com.hcmurs.repositories.apis.request.RequestRepository
import org.com.hcmurs.repositories.apis.station.BusStationApi
import org.com.hcmurs.repositories.apis.station.BusStationRepository
import org.com.hcmurs.repositories.apis.station.MetroStationApi
import org.com.hcmurs.repositories.apis.station.MetroStationRepository
import org.com.hcmurs.repositories.apis.station.StationApi
import org.com.hcmurs.repositories.apis.station.StationRepository
import org.com.hcmurs.repositories.apis.ticket.FareMatrixApi
import org.com.hcmurs.repositories.apis.ticket.FareMatrixRepository
import org.com.hcmurs.repositories.apis.ticket.TicketApi
import org.com.hcmurs.repositories.apis.ticket.TicketRepository
import org.com.hcmurs.repositories.apis.user.ProfileApi
import org.com.hcmurs.repositories.apis.weather.WeatherApi
import org.com.hcmurs.repositories.apis.weather.WeatherRepository
import org.com.hcmurs.security.TokenProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val BASE_STATION = "http://192.168.88.172:4004/"
        private const val BASE_STATION_ = "http://10.0.2.2:4004/"
        private const val BASE_WEATHER_URL = "https://api.open-meteo.com/v1/"
        private const val BASE_CURRENCY_URL = "https://api.exchangerate-api.com/"
        private const val BASE_URL = "http://192.168.88.172:4003/"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    @Named("Auth")
    fun provideAuthInterceptor(tokenProvider: TokenProvider): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val token = tokenProvider.getToken()

        val requestBuilder: Request.Builder = original.newBuilder()
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideTokenProvider(@ApplicationContext context: Context): TokenProvider {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return SharedPreferencesTokenProvider(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("Auth") authInterceptor: Interceptor,
        @Named("ApiKey") apiKeyInterceptor: Interceptor,
        cookieManager: CookieManager,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            },
        )
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    @Named("ApiKey")
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Content-Type", "application/json") // luôn set JSON
            .header(
                "x-api-key",
                "c761c9f0bb379612afbfd6ffeca90261db961bb93bce17728bc2a74430a66c0a",
            )
            .build()
        chain.proceed(request)
    }

    // metro station api
    @Provides
    @Singleton
    fun provideMetroStationApi(
        @Named("stationRetrofit") retrofit: Retrofit,
    ): MetroStationApi = retrofit.create(MetroStationApi::class.java)

    @Provides
    @Named("stationRetrofit")
    @Singleton
    fun provideStationRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_STATION_)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideMetroStationRepository(api: MetroStationApi): MetroStationRepository = MetroStationRepository(api)

    // mock bus station api
    @Provides
    @Singleton
    fun provideBusStationApi(
        @Named("busRetrofit") retrofit: Retrofit,
    ): BusStationApi = retrofit.create(BusStationApi::class.java)

    @Provides
    @Named("busRetrofit")
    @Singleton
    fun provideMockyRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_STATION)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideBusStationRepository(api: BusStationApi): BusStationRepository = BusStationRepository(api)

    @Provides
    @Singleton
    fun provideTicketApi(
        retrofit: Retrofit,
    ): TicketApi = retrofit.create(TicketApi::class.java)

    // Provide the TicketRepository
    @Provides
    @Singleton
    fun provideTicketRepository(api: TicketApi): TicketRepository = TicketRepository(api)

    @Provides
    @Singleton
    fun provideFareMatrixApi(
        retrofit: Retrofit,
    ): FareMatrixApi = retrofit.create(FareMatrixApi::class.java)

    @Provides
    @Singleton
    fun provideFareMatrixRepository(api: FareMatrixApi): FareMatrixRepository = FareMatrixRepository(api)

    // Stations
    @Provides
    @Singleton
    fun provideStationApi(
        retrofit: Retrofit,
    ): StationApi = retrofit.create(StationApi::class.java)

    @Provides
    @Singleton
    fun provideStationRepository(api: StationApi): StationRepository = StationRepository(api)

    // order
    @Provides
    @Singleton
    fun provideOrderApi(
        retrofit: Retrofit,
    ): OrderSingleApi = retrofit.create(OrderSingleApi::class.java)

    @Provides
    @Singleton
    fun provideOrdersApi(retrofit: Retrofit): OrderDaysApi = retrofit.create(OrderDaysApi::class.java)

    @Provides
    @Singleton
    fun provideOrdersRepository(api: OrderDaysApi): OrderDaysRepository = OrderDaysRepository(api)

    // weather
    @Provides
    @Singleton
    @Named("weatherRetrofit")
    fun provideWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_WEATHER_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideWeatherApi(
        @Named("weatherRetrofit") retrofit: Retrofit,
    ): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository = WeatherRepository(api)

    // Blog
    @Provides
    @Singleton
    @Named("publicClient")
    fun providePublicOkHttpClient(
        @Named("ApiKey") apiKeyInterceptor: Interceptor,
        cookieManager: CookieManager,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            },
        )
        .cookieJar(JavaNetCookieJar(cookieManager))
        .build()

    @Provides
    @Named("publicRetrofit")
    @Singleton
    fun providePublicRetrofit(@Named("publicClient") okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providePublicBlogApi(@Named("publicRetrofit") retrofit: Retrofit): PublicBlogApi = retrofit.create(PublicBlogApi::class.java)

    @Provides
    @Singleton
    fun providePublicBlogRepository(api: PublicBlogApi): BlogRepository = BlogRepository(api)

    // request
    @Provides
    @Singleton
    fun provideRequestApi(retrofit: Retrofit): RequestApi = retrofit.create(RequestApi::class.java)

    @Provides
    @Singleton
    fun provideRequestRepository(api: RequestApi): RequestRepository = RequestRepository(api)

    // feedback
    @Provides
    @Singleton
    fun provideFeedbackApi(retrofit: Retrofit): FeedbackApi = retrofit.create(FeedbackApi::class.java)

    @Provides
    @Singleton
    fun provideFeedbackRepository(api: FeedbackApi): FeedbackRepository = FeedbackRepository(api)

    // payment
    @Provides
    @Singleton
    fun providePaymentApi(retrofit: Retrofit): PaymentApi = retrofit.create(PaymentApi::class.java)

    @Provides
    @Singleton
    fun providePaymentRepository(api: PaymentApi): PaymentRepository = PaymentRepository(api)

    // Currency API
    @Provides
    @Named("currencyRetrofit")
    @Singleton
    fun provideCurrencyRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_CURRENCY_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideCurrencyApi(@Named("currencyRetrofit") retrofit: Retrofit): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    // Notification API
    @Provides
    @Singleton
    fun provideNotificationApi(okHttpClient: OkHttpClient): NotificationApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NotificationApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationRepository(
        api: NotificationApi,
        tokenProvider: SharedPreferencesTokenProvider,
        authRepository: AuthRepository,
    ): NotificationRepository = NotificationRepository(api, tokenProvider, authRepository)
}
