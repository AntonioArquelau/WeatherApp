package com.example.weatherapp.model.repository

import android.util.Log
import com.example.weatherapp.api.WeatherApi
import com.example.weatherapp.model.data.WeatherResponse
import com.example.weatherapp.utils.DataStatus
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.lang.Error

class WeatherRepository() {

    companion object {
        private const val TAG = "WeatherRepository"
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .build()
            .create(WeatherApi::class.java)
    }

    suspend fun getWeatherBasedOnLocation(latitude: String, longitude: String, appId: String) = flow<DataStatus<WeatherResponse>> {
        emit(DataStatus.Loading())
        val result =  weatherApi.get(latitude, longitude, appId)
        when(result.code()){
            200 -> {emit(DataStatus.Success(result.body()))}
            400 -> {emit(DataStatus.Error(null, result.message()))}
            500 -> {emit(DataStatus.Error(null, result.message()))}
        }

    }.catch {
        emit(DataStatus.Error(null, it.message.toString()))
    }.flowOn(Dispatchers.IO)
}