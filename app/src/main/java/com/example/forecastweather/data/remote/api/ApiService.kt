package com.example.forecastweather.data.remote.api

import com.example.forecastweather.data.remote.dto.CityDto
import com.example.forecastweather.data.remote.dto.CurrentWeatherDto
import com.example.forecastweather.data.remote.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("current.json")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): CurrentWeatherDto

    @GET("forecast.json")
    suspend fun loadForecastWeather(
        @Query("q") query: String,
        @Query("days") count: Int = DEFAULT_COUNT_FORECAST_DAYS
    ): WeatherForecastDto

    @GET("search.json")
    suspend fun searchCity(
        @Query("q") query: String
    ): List<CityDto>

    companion object {
        private const val DEFAULT_COUNT_FORECAST_DAYS = 7
    }
}