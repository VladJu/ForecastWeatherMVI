package com.example.forecastweather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConditionDto(
    @SerializedName("text") val text : String,
    @SerializedName("icon") val icon : String,
)