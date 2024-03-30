package com.loverian.weatherapp.view

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.bumptech.glide.Glide
import com.loverian.weatherapp.BuildConfig
import com.loverian.weatherapp.R
import com.loverian.weatherapp.databinding.ActivityMainBinding
import com.loverian.weatherapp.model.WeatherInfoShowModel
import com.loverian.weatherapp.model.WeatherInfoShowModelImpl
import com.loverian.weatherapp.model.entities.City
import com.loverian.weatherapp.model.entities.LocationFields
import com.loverian.weatherapp.model.entities.WeatherData
import com.loverian.weatherapp.utils.convertToListOfCityName
import com.loverian.weatherapp.viewmodel.WeatherInfoViewModel
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var model: WeatherInfoShowModel
    private lateinit var viewModel: WeatherInfoViewModel
    private lateinit var binding: ActivityMainBinding
    private var currentLocation: LocationFields = LocationFields(0.0, 0.0)

    private var cityList: MutableList<City> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        model = WeatherInfoShowModelImpl(applicationContext)
        viewModel = ViewModelProviders.of(this).get(WeatherInfoViewModel::class.java)

        val lekuActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    Log.d("RESULT****", "OK")
                    val latitude = data?.getDoubleExtra(LATITUDE, 0.0)
                    val longitude = data?.getDoubleExtra(LONGITUDE, 0.0)
                    val city = data?.getStringExtra(LOCATION_ADDRESS)
                    if (latitude != null && longitude != null) {
                        currentLocation = LocationFields(
                            latitude,longitude
                        )
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                        val cityName: String = addresses!![0].getAddressLine(0)
                        if (cityName != null && cityName.length > 18) {
                            binding.layoutInput.location.text = cityName.substring(0, 13) + "..."
                        } else {
                            binding.layoutInput.location.text = cityName
                        }
                        binding.layoutInput.location.text = cityName
                    }
                  Log.d("CITY****", city.toString())

                } else {
                    Log.d("RESULT****", "CANCELLED")
                }
            }

        // places api requires billing enabled on GCP account. hence it will not work
        val locationPickerIntent = LocationPickerActivity.Builder(applicationContext)
            .withGeolocApiKey(BuildConfig.MAP_KEY)
            .withGooglePlacesApiKey(BuildConfig.PLACES_API_KEY)
            .withSearchZone("en_IN")
            .withDefaultLocaleSearchZone()
            .shouldReturnOkOnBackPressed()
            .withStreetHidden()
            .withZipCodeHidden()
            .withSatelliteViewHidden()
            .withGooglePlacesEnabled()
            .withGoogleTimeZoneEnabled()
            .withVoiceSearchHidden()
            .build()

        setLiveDataListeners()
        setViewClickListener(lekuActivityResultLauncher, locationPickerIntent)
    }

    private fun setViewClickListener(lekuActivityResultLauncher: ActivityResultLauncher<Intent>, locationPickerIntent: Intent){
        binding.layoutInput.btnViewWeather.setOnClickListener {
            viewModel.getWeatherInfo(currentLocation.lat,currentLocation.lon, model) // fetch weather info

        }
        binding.layoutInput.location.setOnClickListener {
            lekuActivityResultLauncher.launch(locationPickerIntent)
        }
    }

    private fun setLiveDataListeners() {

        viewModel.progressBarLiveData.observe(this, Observer { isShowLoader ->
            if (isShowLoader)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE
        })

        viewModel.weatherInfoLiveData.observe(this, Observer { weatherData ->
            setWeatherInfo(weatherData)
        })

        viewModel.weatherInfoFailureLiveData.observe(this, Observer { errorMessage ->
            binding.outputGroup.visibility = View.GONE
            binding.tvErrorMessage.visibility = View.VISIBLE
            binding.tvErrorMessage.text = errorMessage
        })
    }

    private fun setWeatherInfo(weatherData: WeatherData) {
        binding.outputGroup.visibility = View.VISIBLE
        binding.tvErrorMessage.visibility = View.GONE

        binding.layoutWeatherBasic.tvDateTime?.text = weatherData.dateTime
        binding.layoutWeatherBasic.tvTemperature?.text = weatherData.temperature
        binding.layoutWeatherBasic.tvCityCountry?.text = weatherData.cityAndCountry
        Glide.with(this).load(weatherData.weatherConditionIconUrl).into(binding.layoutWeatherBasic.ivWeatherCondition)
        binding.layoutWeatherBasic.tvWeatherCondition?.text = weatherData.weatherConditionIconDescription

        binding.layoutWeatherAdditional.tvHumidityValue?.text = weatherData.humidity
        binding.layoutWeatherAdditional.tvPressureValue?.text = weatherData.pressure
        binding.layoutWeatherAdditional.tvVisibilityValue?.text = weatherData.visibility

        binding.layoutSunsetSunrise.tvSunriseTime?.text = weatherData.sunrise
        binding.layoutSunsetSunrise.tvSunsetTime?.text = weatherData.sunset
    }
}