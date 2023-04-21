package com.example.weatherapp.network

import com.example.weatherapp.models.forecast.City
import com.example.weatherapp.models.forecast.Coord
import com.example.weatherapp.models.forecast.Forecast
import com.example.weatherapp.models.Coordinates
import com.example.weatherapp.models.WeatherResult
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
): WeatherRepository {

    override suspend fun getLocationCoordinates(city: String): Coordinates {

        val response = weatherApi.getLocation(city, LIMIT, APP_ID)
        if (response.isSuccessful) {
            val locationResult = response.body()
            val lat = locationResult?.first()?.lat
            val lon = locationResult?.first()?.lon
            if (lat != null && lon != null) {
                return Coordinates(lat, lon)
            }
        }
        return Coordinates(0.0, 0.0)
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResult {
        val response = weatherApi.getCurrentWeather(lat, lon, APP_ID, METRIC)
        if (response.isSuccessful) {
            val weatherResult = response.body()
            return WeatherResult(
                main = weatherResult?.weather?.first()?.main ?: "",
                description = weatherResult?.weather?.first()?.description ?: "",
                temp = weatherResult?.main?.temp ?: 0.0,
                pressure = weatherResult?.main?.pressure ?: 0,
                humidity = weatherResult?.main?.humidity ?: 0,
                windSpeed = weatherResult?.wind?.speed ?: 0.0
            )
        }
        return WeatherResult(
            main = "",
            description = "",
            temp = 0.0,
            pressure = 0,
            humidity = 0,
            windSpeed = 0.0
        )
    }

    override suspend fun getForecast(lat: Double, lon: Double): Forecast {
        val response = weatherApi.getForecast(lat, lon, APP_ID, METRIC)
        if (response.isSuccessful) {
            val forecastResult = response.body()
            return Forecast(
                city = forecastResult?.city ?: City(Coord(0.0, 0.0), "", 0, "", 0, 0, 0, 0),
                cnt = forecastResult?.cnt ?: 0,
                list = forecastResult?.list ?: listOf(),
                cod = forecastResult?.cod ?: "",
                message = forecastResult?.message ?: 0
            )
        }

        return Forecast(
            city = City(Coord(0.0, 0.0), "", 0, "", 0, 0, 0, 0),
            cnt = 0,
            list = listOf(),
            cod = "",
            message = 0
        )
    }

    companion object {
        const val LIMIT = "1"
        const val APP_ID = "8f43da1d5371b3d1d29780bd1ff3c34b"
        const val METRIC = "metric"
        const val WEATHER_TYPE_CLEAR = "Clear"
        const val WEATHER_TYPE_RAIN = "Drizzle"
        const val WEATHER_TYPE_SNOW = "Snow"
        const val WEATHER_TYPE_THUNDERSTORM = "Thunderstorm"
        const val WEATHER_TYPE_CLOUDS = "Clouds"
    }
}