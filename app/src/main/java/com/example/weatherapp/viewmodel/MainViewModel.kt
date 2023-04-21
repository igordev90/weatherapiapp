package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.network.WeatherRepository
import com.example.weatherapp.models.forecast.Forecast
import com.example.weatherapp.models.Coordinates
import com.example.weatherapp.models.WeatherResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val coordinates: MutableLiveData<Coordinates> = MutableLiveData()
    val coordinatesResult: LiveData<Coordinates> = coordinates

    private val currentWeather: MutableLiveData<WeatherResult> = MutableLiveData()
    val currentWeatherResult: LiveData<WeatherResult> = currentWeather

    private val forecast: MutableLiveData<Forecast> = MutableLiveData()
    val forecastResult: LiveData<Forecast> = forecast

    suspend fun getCoordinates(city : String){
        val coordinatesApiResult = weatherRepository.getLocationCoordinates(city)
        coordinates.postValue(coordinatesApiResult)
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double){
        val currentWeatherResult = weatherRepository.getCurrentWeather(lat,lon)
        currentWeather.postValue(currentWeatherResult)
    }

    suspend fun getForecast(lat: Double, lon: Double){
        val currentForecast = weatherRepository.getForecast(lat, lon)
        forecast.postValue(currentForecast)
    }
}