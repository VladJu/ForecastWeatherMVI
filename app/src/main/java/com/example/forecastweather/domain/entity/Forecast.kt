package com.example.forecastweather.domain.entity

data class Forecast(
    val currentWeather : Weather,
    val upcomingForecast : List<Weather>
)
