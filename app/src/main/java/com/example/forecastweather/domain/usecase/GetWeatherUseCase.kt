package com.example.forecastweather.domain.usecase

import com.example.forecastweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId:Int) = repository.getWeather(cityId)

}