package com.example.weatherapp.model.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse (

    val name: String,
    @SerializedName("coord")
    val coordinator : Coordinator,
    val weather : List<WeatherSummary>,
    @SerializedName("timezone")
    val timeZone: String,
    @SerializedName("main")
    val temperature: Temperature
)