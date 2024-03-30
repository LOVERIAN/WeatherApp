package com.loverian.weatherapp.network

import com.loverian.weatherapp.model.entities.WeatherInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    fun callApiForWeatherInfo(@Query("id") cityId: Int): Call<WeatherInfoResponse>
    @GET("weather")
    fun callApiForWeatherInfo(@Query("lat") lat: Double, @Query("lon") lon: Double): Call<WeatherInfoResponse>
}