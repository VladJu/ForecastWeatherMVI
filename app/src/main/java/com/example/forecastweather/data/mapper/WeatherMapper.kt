package com.example.forecastweather.data.mapper

import com.example.forecastweather.data.remote.dto.CurrentWeatherDto
import com.example.forecastweather.data.remote.dto.WeatherDto
import com.example.forecastweather.data.remote.dto.WeatherForecastDto
import com.example.forecastweather.domain.entity.Forecast
import com.example.forecastweather.domain.entity.Weather
import java.util.Calendar
import java.util.Date


fun CurrentWeatherDto.toEntity(): Weather = weatherDto.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    conditionText = conditionDto.text,
    conditionUrl = conditionDto.icon.correctImageUrl(),
    date = date.toCalendar()
)


fun WeatherForecastDto.toEntity(): Forecast = Forecast(
    currentWeather = weather.toEntity(),
    upcomingForecast = forecastDto.forecastDayDto.map { forecastDayDto ->
        val weatherDayDto = forecastDayDto.weatherDayDto
        Weather(
            tempC = weatherDayDto.tempC,
            conditionUrl = weatherDayDto.conditionDto.icon.correctImageUrl(),
            conditionText = weatherDayDto.conditionDto.text,
            date = forecastDayDto.date.toCalendar()
        )
    }
)


private fun Long.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = Date(this@toCalendar * SECONDS_TO_MILLISECONDS)
}

private fun String.correctImageUrl(): String = "https:$this".replace(
    oldValue = "64x64",
    newValue = "128X128"
)

private const val SECONDS_TO_MILLISECONDS = 1000