package com.example.chasechallenge.network.api

import com.example.chasechallenge.BuildConfig
import com.example.chasechallenge.network.response.CityWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/data/2.5/weather")
    suspend fun getWeatherDataByLatitudeLongitude(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "imperial",
        @Query("appid") key: String = BuildConfig.API_KEY
        ): Response<CityWeatherResponse>
}