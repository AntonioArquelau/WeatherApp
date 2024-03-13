package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.data.WeatherResponse
import com.example.weatherapp.model.repository.WeatherRepository
import com.example.weatherapp.utils.DataStatus
import kotlinx.coroutines.launch

class WeatherViewModel :ViewModel() {

    companion object {
        private const val TAG = "WeatherViewModel"
    }
    private val weatherInfoLiveData = MutableLiveData<DataStatus<WeatherResponse>>()

    private val _mainSummary = MutableLiveData<String>()
    private val _city = MutableLiveData("")
    private val _temp = MutableLiveData<String>()
    private val _maxTemp = MutableLiveData<String>()
    private val _minTemp = MutableLiveData<String>()
    private val _humidity = MutableLiveData<String>()
    private val _wind = MutableLiveData<String>()

    val mainSummary: LiveData<String> = _mainSummary
    val city: LiveData<String> = _city
    val temp: LiveData<String> = _temp
    val maxTemp: LiveData<String> = _maxTemp
    val minTemp: LiveData<String> = _minTemp
    val humidity: LiveData<String> = _humidity
    val wind: LiveData<String> = _wind


    private val weatherRepository: WeatherRepository by lazy {
        WeatherRepository()
    }

    fun getTemp(): MutableLiveData<DataStatus<WeatherResponse>>{
        return weatherInfoLiveData
    }

    fun getWeatherBasedOnLocation(latitude: String, longitude: String, appId: String){
        viewModelScope.launch {
            weatherRepository.getWeatherBasedOnLocation(latitude, longitude, appId).collect {
                weatherInfoLiveData.postValue(it)
                it.data?.weather.let {it2 ->
                    if (it is DataStatus.Success)
                        _mainSummary.postValue(it2!![0].main)
                }

            }
        }
    }
}