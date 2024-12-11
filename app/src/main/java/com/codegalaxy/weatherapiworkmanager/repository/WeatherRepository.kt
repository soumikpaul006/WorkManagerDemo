package com.codegalaxy.weatherapiworkmanager.repository

import com.codegalaxy.weatherapiworkmanager.dto.WeatherResponse
import com.codegalaxy.weatherapiworkmanager.network.WeatherApiService

class WeatherRepository(
    private val apiService: WeatherApiService
): IRepository {
    override suspend fun fetchWeather(city: String): WeatherResponse {
        return apiService.getWeather("dd0e2dd98399441aa29141952240212",city)
    }
}