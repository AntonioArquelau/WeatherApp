package com.example.weatherapp.model.data

import com.google.gson.annotations.SerializedName

data class Temperature(
    @SerializedName("temp")
    val day: Float,
    @SerializedName("temp_min")
    val min: Float,
    @SerializedName("temp_max")
    val max: Float,
    @SerializedName("humidity")
    val humidity: Float,
)
