package com.example.forecastweather.presenttion.detail

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun onClickChangeFavouriteStatus()

    fun onClickBack()
}