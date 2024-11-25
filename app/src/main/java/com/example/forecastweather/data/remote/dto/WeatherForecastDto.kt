package com.example.forecastweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("current") val weather: WeatherDto,
    @SerializedName("forecast") val forecastDto: ForecastDto
)