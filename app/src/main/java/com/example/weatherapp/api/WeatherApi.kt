package com.example.weatherapp.api

import com.example.weatherapp.model.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface WeatherApi {
    @GET("weather")
    fun get(@Query("lat") latitude: String,
            @Query("lon") longitude: String,
            @Query("appid") appId: String): Call<WeatherResponse>
}