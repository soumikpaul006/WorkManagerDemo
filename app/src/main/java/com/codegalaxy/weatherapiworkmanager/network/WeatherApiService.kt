package com.codegalaxy.weatherapiworkmanager.network

import com.codegalaxy.weatherapiworkmanager.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {

    @GET("current.json")
    suspend fun getWeather(
        @Query("key") apiKey:String,
        @Query("q") city:String
    ): WeatherResponse
}
