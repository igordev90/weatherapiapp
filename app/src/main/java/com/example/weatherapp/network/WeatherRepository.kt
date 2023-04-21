package com.example.weatherapp.network

import com.example.weatherapp.models.forecast.Forecast
import com.example.weatherapp.models.Coordinates
import com.example.weatherapp.models.WeatherResult

/**
 * Provides API connection with https://openweathermap.org/
 */
interface WeatherRepository {

    /**
     * Getting location info like lat and lon
     */
    suspend fun getLocationCoordinates(city: String): Coordinates

    /**
     * Getting current weather for specific place by provide lat and lon
     */
    suspend fun getCurrentWeather(lat: Double, lon: Double) : WeatherResult

    /**
     * Getting forecast for specific place by provide lat and lon
     */
    suspend fun getForecast(lat: Double, lon: Double): Forecast
}