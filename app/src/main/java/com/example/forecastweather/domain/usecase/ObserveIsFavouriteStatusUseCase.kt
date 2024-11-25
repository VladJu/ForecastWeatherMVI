package com.example.forecastweather.domain.usecase

import com.example.forecastweather.domain.repository.FavouriteRepository
import javax.inject.Inject

class ObserveIsFavouriteStatusUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke(cityId: Int) = repository.observeIsFavourite(cityId)
}