package com.example.forecastweather.presenttion.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.forecastweather.presenttion.detail.DetailsComponent
import com.example.forecastweather.presenttion.favourite.FavouriteComponent
import com.example.forecastweather.presenttion.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Favourite(val component: FavouriteComponent) : Child

        data class Search(val component: SearchComponent) : Child

        data class Detail(val component: DetailsComponent) : Child
    }

}