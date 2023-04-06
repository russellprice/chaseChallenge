package com.example.chasechallenge.network.repositories

import com.example.chasechallenge.network.api.WeatherAPI
import com.example.chasechallenge.network.handleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherAPI
) {
    suspend fun fetchWeather(latitude: Double, longitude: Double) = withContext(Dispatchers.IO) {
        handleApi {
            api.getWeatherDataByLatitudeLongitude(latitude, longitude)
        }
    }
}