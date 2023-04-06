package com.example.chasechallenge.network.repositories

import com.example.chasechallenge.db.CityGeoDataDao
import com.example.chasechallenge.network.NetworkResult
import com.example.chasechallenge.network.api.GeoAPI
import com.example.chasechallenge.network.handleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoRepository @Inject constructor(
    private val api: GeoAPI,
    private val cityGeoDataDao: CityGeoDataDao
) {


    suspend fun fetchCity(longitude: Double, latitude: Double) = withContext(Dispatchers.IO) {
        val data = cityGeoDataDao.get()
        if (data != null) {
            NetworkResult.Success(listOf(data))
        } else {
            handleApi {
                api.getGeoDataByLatitudeLongitude(latitude = latitude, longitude = longitude)
            }
        }
    }

    suspend fun fetchCity(name: String) = withContext(Dispatchers.IO) {
        handleApi {
            api.getGeoDataByName(name = name)
        }
    }
}