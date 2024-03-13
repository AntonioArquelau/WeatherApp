package com.example.weatherapp.utils

import kotlin.Exception

sealed class DataStatus<T>(
    val data: T? = null,
    val exception: String? = null,
) {
    class Loading<T>() : DataStatus<T>()
    class Success<T>(data: T?) : DataStatus<T>(data)
    class Error<T>(data: T? = null, exception: String) : DataStatus<T>(data, exception)
}