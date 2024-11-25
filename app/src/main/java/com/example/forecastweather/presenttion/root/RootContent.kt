package com.example.forecastweather.presenttion.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.forecastweather.presenttion.detail.DetailsContent
import com.example.forecastweather.presenttion.favourite.FavouriteContent
import com.example.forecastweather.presenttion.search.SearchContent
import com.example.forecastweather.presenttion.ui.theme.ForecastWeatherTheme

@Composable
fun RootContent(component: RootComponent) {
    ForecastWeatherTheme {
        Children(stack = component.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.Detail -> {
                    DetailsContent(component = instance.component)
                }

                is RootComponent.Child.Favourite -> {
                    FavouriteContent(component = instance.component)
                }

                is RootComponent.Child.Search -> {
                    SearchContent(component = instance.component)
                }
            }
        }

    }
}
