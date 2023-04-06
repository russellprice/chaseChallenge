package com.example.chasechallenge.main

import android.view.View
import com.example.chasechallenge.R
import com.example.chasechallenge.databinding.ItemCityDataBinding
import com.example.chasechallenge.network.response.CityGeoResponse
import com.example.chasechallenge.network.response.CityWeatherResponse

import com.xwray.groupie.viewbinding.BindableItem

class CityItem(private val cityGeo: CityGeoResponse, private val weather: CityWeatherResponse?): BindableItem<ItemCityDataBinding>() {

    override fun initializeViewBinding(view: View): ItemCityDataBinding {
        return ItemCityDataBinding.bind(view)
    }

    override fun getLayout() = R.layout.item_city_data

    override fun bind(binding: ItemCityDataBinding, position: Int) {
        binding.apply {
            name.text = "${cityGeo.name}, ${cityGeo.state}"
            country.text = cityGeo.country
            longitude.text = cityGeo.lon.toString()
            latitude.text = cityGeo.lat.toString()
            currentWeather.text = weather?.main?.temp.toString()
        }
    }
}