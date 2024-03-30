package com.loverian.weatherapp.utils

import com.loverian.weatherapp.model.entities.City
import java.text.SimpleDateFormat
import java.util.*

fun Int.unixTimestampToDateTimeString() : String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this*1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault() // user's default time zone
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun Int.unixTimestampToTimeString() : String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this*1000.toLong()

        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun MutableList<City>.convertToListOfCityName() : MutableList<String> {

    val cityNameList: MutableList<String> = mutableListOf()

    for (city in this) {
        cityNameList.add(city.name)
    }

    return  cityNameList
}


fun Double.kelvinToCelsius() : Int {

    return  (this - 273.15).toInt()
}