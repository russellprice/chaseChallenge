package com.example.chasechallenge.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chasechallenge.db.CityGeoDataDao
import com.example.chasechallenge.network.repositories.GeoRepository
import com.example.chasechallenge.network.NetworkResult
import com.example.chasechallenge.network.repositories.WeatherRepository
import com.example.chasechallenge.network.response.CityGeoResponse
import com.example.chasechallenge.network.response.CityWeatherResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val geoRepository: GeoRepository,
    private val weatherRepository: WeatherRepository,
    private val cityGeoDataDao: CityGeoDataDao
) :
    ViewModel() {

    // city for when the user types
    val city: MutableStateFlow<String> = MutableStateFlow("")

    // list of cities already searched for
    val cities: StateFlow<List<Pair<CityGeoResponse, CityWeatherResponse?>>> = MutableStateFlow(emptyList())

    init {
        //init the collection of the user typing
        viewModelScope.launch {
            city.debounce(1000L).collect {
                if (it.isNotBlank())
                    fetchCity(it)
            }
        }
    }

    // fetch the city based on lat/lon
    fun fetchCity(longitude: Double, latitude: Double) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val data =
                    geoRepository.fetchCity(longitude = longitude, latitude = latitude)) {
                    is NetworkResult.Success -> {
                        (data.data as? MutableList<CityGeoResponse>)?.let {
                            if (cityGeoDataDao.get() == null)
                                cityGeoDataDao.insert(it.first())
                            val weather = fetchWeather(it.first())
                            cities.tryToEmit(listOf( Pair(it.first(), weather)))
                        }
                    }
                    else -> {
                        Log.d("error", "$data")
                    }
                }
            }
        }

    // fetch the city based on the name
    private fun fetchCity(name: String) = viewModelScope.launch {
        when (val data = geoRepository.fetchCity(name)) {
            is NetworkResult.Success -> {
                (data.data as? MutableList<CityGeoResponse>)?.let {
                    if(it.isEmpty()) return@let
                    val weather = fetchWeather(it.first())
                    val temp = (cities.value as MutableList) + Pair(it.first(), weather)
                    cities.tryToEmit(temp.reversed())
                }
            }
            else -> {
                Log.d("error", "name: $name")
            }
        }
    }

    // fetch weather based on a cities geo data
    private suspend fun fetchWeather(geoResponse: CityGeoResponse): CityWeatherResponse? {
        return when(val data = weatherRepository.fetchWeather(geoResponse.lat, geoResponse.lon)) {
            is NetworkResult.Success -> {
                data.data
            } else ->  null
        }
    }

    private fun <T> StateFlow<T>.tryToEmit(data: T) {
        (this as MutableStateFlow).tryEmit(data)
    }
}