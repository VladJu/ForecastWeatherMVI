package com.example.forecastweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CurrentWeatherDto(
    @SerializedName("current") val weatherDto: WeatherDto
)
