package com.example.forecastweather.domain.repository

import com.example.forecastweather.domain.entity.Forecast
import com.example.forecastweather.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast

}