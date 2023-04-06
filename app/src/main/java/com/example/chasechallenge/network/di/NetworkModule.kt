package com.example.chasechallenge.network.di

import com.example.chasechallenge.network.api.GeoAPI
import com.example.chasechallenge.network.api.WeatherAPI
import com.example.chasechallenge.network.response.CityWeatherResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIMEOUT_NUMBER = 15L

    private const val BASE_URL = "https://api.openweathermap.org"

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .readTimeout(TIMEOUT_NUMBER, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_NUMBER, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
        .addConverterFactory(gsonConverterFactory).build()

    @Provides
    fun provideGeoAPI(retrofit: Retrofit): GeoAPI = retrofit.create(GeoAPI::class.java)

    @Provides
    fun providesWeatherAPI(retrofit: Retrofit): WeatherAPI = retrofit.create(WeatherAPI::class.java)
}