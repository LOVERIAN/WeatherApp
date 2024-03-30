package com.loverian.weatherapp.model

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import com.loverian.weatherapp.common.RequestCompleteListener
import com.loverian.weatherapp.model.entities.City
import com.loverian.weatherapp.model.entities.WeatherInfoResponse
import com.loverian.weatherapp.network.ApiInterface
import com.loverian.weatherapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherInfoShowModelImpl(private val context: Context): WeatherInfoShowModel {

    override fun getWeatherInfo(lat: Double,long: Double, callback: RequestCompleteListener<WeatherInfoResponse>) {

        val apiInterface: ApiInterface = RetrofitClient.client.create(ApiInterface::class.java)
        val call: Call<WeatherInfoResponse> = apiInterface.callApiForWeatherInfo(lat,long)

        call.enqueue(object : Callback<WeatherInfoResponse> {

            // if retrofit network call success, this method will be triggered
            override fun onResponse(call: Call<WeatherInfoResponse>, response: Response<WeatherInfoResponse>) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            // this method will be triggered if network call failed
            override fun onFailure(call: Call<WeatherInfoResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!) //let presenter know about failure
            }
        })
    }
}