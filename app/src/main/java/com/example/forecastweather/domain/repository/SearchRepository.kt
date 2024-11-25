package com.example.forecastweather.domain.repository

import com.example.forecastweather.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}