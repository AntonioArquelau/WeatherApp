package com.example.weatherapp.utils

import java.text.DecimalFormat
import java.text.NumberFormat

class TemperatureUtils {
    companion object{
        fun convertKelvinToCelsius(tempKelvin: Float): String {
            val formatter: NumberFormat = DecimalFormat("#0.00")
            val celsiusTemp = formatter.format(tempKelvin - 273.15f)
            return celsiusTemp
        }
    }
}