package com.uranihrc.weatherapp.domain.repository

import com.uranihrc.weatherapp.domain.util.Resource
import com.uranihrc.weatherapp.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat:Double, long: Double): Resource<WeatherInfo>
}