package com.example.weatherapp.api

import com.example.weatherapp.model.data.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun get(@Query("lat") latitude: String,
                    @Query("lon") longitude: String,
                    @Query("appid") appid: String): Response<WeatherResponse>
}