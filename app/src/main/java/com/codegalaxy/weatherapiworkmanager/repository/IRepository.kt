package com.codegalaxy.weatherapiworkmanager.repository

import com.codegalaxy.weatherapiworkmanager.dto.WeatherResponse

interface IRepository {
    suspend fun fetchWeather(city:String): WeatherResponse
}