package com.example.forecastweather.domain.usecase

import com.example.forecastweather.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query:String) = repository.search(query)
}