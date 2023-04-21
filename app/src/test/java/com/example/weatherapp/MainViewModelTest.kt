package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.network.WeatherRepository
import com.example.weatherapp.models.forecast.City
import com.example.weatherapp.models.forecast.Coord
import com.example.weatherapp.models.forecast.Forecast
import com.example.weatherapp.models.Coordinates
import com.example.weatherapp.models.WeatherResult
import com.example.weatherapp.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val weatherRepositoryMock: WeatherRepository = mock()


    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(weatherRepositoryMock)
    }

    @Test
    fun `test getCoordinates`() = runBlocking {
        val city = "TestCity"
        val coordinates = Coordinates(1.0, 1.0)
        Mockito.`when`(weatherRepositoryMock.getLocationCoordinates(city)).thenReturn(coordinates)
        viewModel.getCoordinates(city)
        assertEquals(coordinates, viewModel.coordinatesResult.value)
    }

    @Test
    fun `test getCurrentWeather`() = runBlocking {
        val lat = 0.0
        val lon = 0.0
        val weatherResult = WeatherResult("main", "description", 1.0, 1, 1, 1.1)
        `when`(weatherRepositoryMock.getCurrentWeather(lat, lon)).thenReturn(weatherResult)
        viewModel.getCurrentWeather(lat, lon)
        assertEquals(weatherResult, viewModel.currentWeatherResult.value)
    }

    @Test
    fun `test getForecast`() = runBlocking {
        val lat = 0.0
        val lon = 0.0
        val forecast = Forecast(
            City(Coord(1.0, 1.0), "Country", 1, "name", 100, 200, 201, 31), 1, "cod",
            emptyList(), 1
        )
        `when`(weatherRepositoryMock.getForecast(lat, lon)).thenReturn(forecast)
        viewModel.getForecast(lat, lon)
        assertEquals(forecast, viewModel.forecastResult.value)
    }
}