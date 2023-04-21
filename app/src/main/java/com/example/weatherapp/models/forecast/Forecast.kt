package com.example.weatherapp.models.forecast

data class Forecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherList>,
    val message: Int
)