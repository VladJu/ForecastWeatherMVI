package com.example.forecastweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherDayDto(
    @SerializedName("avgtemp_c") val tempC: Float,
    @SerializedName("condition") val conditionDto: ConditionDto

)
