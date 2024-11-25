package com.example.forecastweather.data.repository_impl

import com.example.forecastweather.data.mapper.toEntity
import com.example.forecastweather.data.remote.api.ApiService
import com.example.forecastweather.domain.entity.Forecast
import com.example.forecastweather.domain.entity.Weather
import com.example.forecastweather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {

    override suspend fun getForecast(cityId: Int): Forecast {
        return apiService.loadForecastWeather("$PREFIX_CITY_ID$cityId").toEntity()
    }

    override suspend fun getWeather(cityId: Int): Weather {
        return apiService.loadCurrentWeather("$PREFIX_CITY_ID$cityId").toEntity()
    }

    companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}