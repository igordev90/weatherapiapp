package com.example.weatherapp.models

data class LocationItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames?,
    val lon: Double,
    val name: String,
    val state: String
)