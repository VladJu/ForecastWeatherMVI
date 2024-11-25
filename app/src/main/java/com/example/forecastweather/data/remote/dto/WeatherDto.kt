package com.example.forecastweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("last_updated_epoch") val date: Long,
    @SerializedName("condition") val conditionDto: ConditionDto
)
