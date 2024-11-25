package com.example.forecastweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    @SerializedName("date_epoch") val date : Long,
    @SerializedName("day") val weatherDayDto: WeatherDayDto
)
