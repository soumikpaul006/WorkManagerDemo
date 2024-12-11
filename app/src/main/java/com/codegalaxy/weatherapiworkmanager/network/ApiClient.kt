package com.codegalaxy.weatherapiworkmanager.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherApiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)
}