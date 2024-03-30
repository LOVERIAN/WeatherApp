package com.loverian.weatherapp.model

import com.loverian.weatherapp.common.RequestCompleteListener
import com.loverian.weatherapp.model.entities.City
import com.loverian.weatherapp.model.entities.WeatherInfoResponse

interface WeatherInfoShowModel {
    fun getWeatherInfo(lat: Double,long: Double, callback: RequestCompleteListener<WeatherInfoResponse>)
}