package com.example.forecastweather.domain.usecase

import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.repository.FavouriteRepository
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(city: City) = repository.addToFavourite(city)
}