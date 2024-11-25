package com.example.forecastweather.presenttion.search

import com.example.forecastweather.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun changeQueryResult(query: String)

    fun onClickBack()

    fun onClickSearch()

    fun onClickCity(city: City)


}