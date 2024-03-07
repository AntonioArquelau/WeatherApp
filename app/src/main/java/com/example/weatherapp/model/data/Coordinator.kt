package com.example.weatherapp.model.data

import com.google.gson.annotations.SerializedName

data class Coordinator(
    @SerializedName("lat")
    val latitude: Float,
    @SerializedName("lon")
    val longitude: Float
)
