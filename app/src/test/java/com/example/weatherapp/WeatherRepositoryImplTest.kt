package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.network.WeatherRepositoryImpl
import com.example.weatherapp.network.WeatherRepositoryImpl.Companion.LIMIT
import com.example.weatherapp.network.WeatherRepositoryImpl.Companion.APP_ID
import com.example.weatherapp.network.WeatherRepositoryImpl.Companion.METRIC
import com.example.weatherapp.models.forecast.City
import com.example.weatherapp.models.forecast.Forecast
import com.example.weatherapp.models.forecast.Rain
import com.example.weatherapp.models.forecast.WeatherList
import com.example.weatherapp.models.*
import com.example.weatherapp.network.WeatherApi
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import retrofit2.Response

class WeatherRepositoryImplTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val weatherApiMock: WeatherApi = mock()


    private lateinit var subject: WeatherRepositoryImpl

    @Before
    fun setUp() {
        subject = WeatherRepositoryImpl(weatherApiMock)
    }

    @Test
    fun `getLocationCoordinates success response`(): Unit = runBlocking {
        val city = "London"
        val expectedCoordinates = Coordinates(37.7749, -122.4194)
        val location = Location()
        location.add(LocationItem("USA", 37.7749, null, -122.4194, "San Francisco", "CA"))
        location.add(LocationItem("USA", 40.7128, null, -74.0060, "New York", "NY"))
        val mockResponse = Response.success(location)

        `when`(weatherApiMock.getLocation(city, LIMIT, APP_ID)).thenReturn(mockResponse)

        val result = subject.getLocationCoordinates(city)

        assertEquals(expectedCoordinates.lat, result.lat)
        assertEquals(expectedCoordinates.lon, result.lon)
    }

    private val lat = 37.7749
    private val lon = -122.4194

    @Test
    fun `getCurrentWeather success response`(): Unit = runBlocking {
        val location = CurrentWeather(
            "base",
            Clouds(1), 1, Coord(lat, lon), 1, 1,
            Main(1.0, 1, 1, 1, 1, 1.0, 1.0, 1.0), "name",
            Sys(
                "country", 1, 1, 1, 1
            ), 1, 1,
            listOf(Weather("description", "icon", 1, "main")), Wind(1, 1.0, 1.0)
        )
        val mockResponse = Response.success(location)

        `when`(weatherApiMock.getCurrentWeather(lat, lon, APP_ID, METRIC)).thenReturn(mockResponse)

        val result = subject.getCurrentWeather(lat, lon)

        assertEquals("main", result.main)
        assertEquals("description", result.description)
    }

    @Test
    fun getForecast_success() = runBlocking {
        val expectedCity = City(com.example.weatherapp.models.forecast.Coord(lat, lon), "San Francisco", 123, "US", 1620351906, 1620393328, 3600, -25200)
        val expectedForecast = Forecast(expectedCity, 40, "cod", listOf(
            WeatherList(com.example.weatherapp.models.forecast.Clouds(1),1,"dt",com.example.weatherapp.models.forecast.Main(1.0,1,1,1,1,1.1,1.1,1.1,1.1),1.1,
            Rain(1.1),com.example.weatherapp.models.forecast.Sys("pod"),1,
            listOf(com.example.weatherapp.models.forecast.Weather("description","icon",1,"Main")),
                com.example.weatherapp.models.forecast.Wind(1, 1.1, 1.1)
        )
        ), 0)

        val mockResponse = Response.success(expectedForecast)
        `when`(weatherApiMock.getForecast(lat, lon, APP_ID, METRIC)).thenReturn(mockResponse)


        val actualForecast = subject.getForecast(lat, lon)

        assertEquals(expectedForecast, actualForecast)
    }
}