package com.example.chasechallenge

import com.example.chasechallenge.main.CityItem
import com.example.chasechallenge.network.response.CityGeoResponse
import com.example.chasechallenge.network.response.CityWeatherResponse

fun List<Pair<CityGeoResponse, CityWeatherResponse?>>.mapToCityItemList(): List<CityItem> {
    val cityList = mutableListOf<CityItem>()
    for (pair in this) {
        pair.second?.let {
            cityList.add(CityItem(pair.first, it))
        }
    }
    return cityList
}