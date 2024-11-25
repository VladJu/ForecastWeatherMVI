package com.example.forecastweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecastday") val forecastDayDto: List<ForecastDayDto>
)
