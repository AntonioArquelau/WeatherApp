package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.data.WeatherResponse
import com.example.weatherapp.model.repository.WeatherRepository
import com.example.weatherapp.utils.DataStatus
import com.example.weatherapp.utils.TemperatureUtils
import kotlinx.coroutines.launch

class WeatherViewModel :ViewModel() {

    companion object {
        private const val TAG = "WeatherViewModel"
    }
    private val _weatherInfoLiveData = MutableLiveData<DataStatus<WeatherResponse>>()

    private val _mainSummary = MutableLiveData<String>()
    private val _city = MutableLiveData("")
    private val _temp = MutableLiveData<String>()
    private val _maxTemp = MutableLiveData<String>()
    private val _minTemp = MutableLiveData<String>()
    private val _humidity = MutableLiveData<String>()
    private val _wind = MutableLiveData<String>()
    private val _loading = MutableLiveData<Boolean>()

    val mainSummary: LiveData<String> = _mainSummary
    val city: LiveData<String> = _city
    val temp: LiveData<String> = _temp
    val maxTemp: LiveData<String> = _maxTemp
    val minTemp: LiveData<String> = _minTemp
    val humidity: LiveData<String> = _humidity
    val wind: LiveData<String> = _wind
    val infoStatus: LiveData<DataStatus<WeatherResponse>> = _weatherInfoLiveData


    private val weatherRepository: WeatherRepository by lazy {
        WeatherRepository()
    }

    fun getWeatherBasedOnLocation(latitude: String, longitude: String, appId: String){
        _loading.postValue(true)
        viewModelScope.launch {
            weatherRepository.getWeatherBasedOnLocation(latitude, longitude, appId).collect {
                _weatherInfoLiveData.postValue(it)
                when(it) {
                    is DataStatus.Success -> {
                        it.data?.weather.let { it2 ->
                            _mainSummary.postValue(it2!![0].main)
                        }
                        _temp.postValue(it.data?.temperature?.day?.let { kelvinTemp ->
                            TemperatureUtils.convertKelvinToCelsius(kelvinTemp)
                        })
                        _maxTemp.postValue(it.data?.temperature?.max?.let { kelvinTemp ->
                            TemperatureUtils.convertKelvinToCelsius(kelvinTemp)
                        })
                        _minTemp.postValue(it.data?.temperature?.min?.let { kelvinTemp ->
                            TemperatureUtils.convertKelvinToCelsius(kelvinTemp)
                        })
                        _humidity.postValue(it.data?.temperature?.humidity.toString())
                        _city.postValue(it.data?.city.toString())
                        _wind.postValue(it.data?.wind?.speed.toString())
                        _loading.postValue(false)
                    }
                    is DataStatus.Error ->{
                        _loading.postValue(false)
                    }
                    else ->{
                        _loading.postValue(true)
                    }
                }
            }
        }
    }
}