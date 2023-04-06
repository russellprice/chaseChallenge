package com.example.chasechallenge.network.api

import com.example.chasechallenge.BuildConfig
import com.example.chasechallenge.network.response.CityGeoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoAPI {

    @GET("/geo/1.0/direct")
    suspend fun getGeoDataByName(
        @Query("q") name: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") key: String = BuildConfig.API_KEY
    ): Response<List<CityGeoResponse>>

    @GET("/geo/1.0/reverse")
    suspend fun getGeoDataByLatitudeLongitude(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") key: String = BuildConfig.API_KEY
    ): Response<List<CityGeoResponse>>
}