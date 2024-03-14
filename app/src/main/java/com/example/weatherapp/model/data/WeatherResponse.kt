package com.example.weatherapp.model.data

import com.google.gson.annotations.SerializedName

open class WeatherResponse (

    @SerializedName("name")
    val city: String,
    @SerializedName("coord")
    val coordinator : Coordinator,
    @SerializedName("weather")
    val weather : List<WeatherSummary>,
    @SerializedName("wind")
    val wind: Wind,
    @SerializedName("main")
    val temperature: Temperature,
)