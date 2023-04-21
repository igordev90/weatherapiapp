package com.example.weatherapp.di

import com.example.weatherapp.network.WeatherRepository
import com.example.weatherapp.network.WeatherRepositoryImpl
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.network.RetrofitHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideWeatherRepository(weatherApi: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(weatherApi)
    }

    @Provides
    fun provideRequestsApi(): WeatherApi {
        return RetrofitHelper.getInstance().create(WeatherApi::class.java)
    }
}