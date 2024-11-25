package com.example.forecastweather.presenttion.favourite

import com.example.forecastweather.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    val model: StateFlow<FavouriteStore.State>

    fun onClickSearch()

    fun onClickAddToFavourite()

    fun onClickCityItem(city: City)
}