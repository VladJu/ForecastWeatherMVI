package com.example.forecastweather.data.repository_impl

import com.example.forecastweather.data.mapper.toEntities
import com.example.forecastweather.data.remote.api.ApiService
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
        return apiService.searchCity(query).toEntities()
    }
}