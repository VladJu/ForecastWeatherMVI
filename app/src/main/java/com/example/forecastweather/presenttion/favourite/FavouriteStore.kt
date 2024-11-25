package com.example.forecastweather.presenttion.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.usecase.GetFavouriteCitiesUseCase
import com.example.forecastweather.domain.usecase.GetWeatherUseCase
import com.example.forecastweather.presenttion.favourite.FavouriteStore.Intent
import com.example.forecastweather.presenttion.favourite.FavouriteStore.Label
import com.example.forecastweather.presenttion.favourite.FavouriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickSearch : Intent

        data object ClickAddToFavourite : Intent

        data class ClickItemCity(val city: City) : Intent

    }

    data class State(
        val citiItems: List<CityItem>
    ) {
        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {
            data object Initial : WeatherState

            data object Loading : WeatherState

            data object Error : WeatherState

            data class Loaded(
                val tempC: Float,
                val iconUrl: String
            ) : WeatherState
        }
    }

    sealed interface Label {
        data object ClickSearch : Label

        data object ClickAddToFavourite : Label

        data class ClickItemCity(val city: City) : Label

    }
}

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase
) {

    fun create(): FavouriteStore =
        object : FavouriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavouriteStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavouriteCitiesLoaded(val cities: List<City>) : Action

    }

    private sealed interface Msg {
        data class FavouriteCitiesLoaded(val cities: List<City>) : Msg

        data class WeatherLoading(val cityId: Int) : Msg

        data class WeatherLoadingError(val cityId: Int) : Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val iconUrl: String
        ) : Msg

    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect {
                    dispatch(Action.FavouriteCitiesLoaded(it))
                }
            }

        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickAddToFavourite -> {
                    publish(Label.ClickAddToFavourite)
                }

                is Intent.ClickItemCity -> {
                    publish(Label.ClickItemCity(intent.city))
                }

                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }
            }

        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadingWeatherForCity(it.id)
                        }
                    }
                }

            }

        }

        private suspend fun loadingWeatherForCity(cityId: Int) {
            dispatch(Msg.WeatherLoading(cityId))
            try {
                val weather = getWeatherUseCase(cityId)
                dispatch(
                    Msg.WeatherLoaded(
                        cityId = cityId,
                        tempC = weather.tempC,
                        iconUrl = weather.conditionUrl
                    )
                )
            } catch (e: Exception) {
                Msg.WeatherLoadingError(cityId)
            }
        }

    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.FavouriteCitiesLoaded -> {
                    copy(citiItems = msg.cities.map {
                        State.CityItem(
                            city = it,
                            weatherState = State.WeatherState.Initial
                        )
                    })
                }

                is Msg.WeatherLoaded -> {
                    copy(citiItems = citiItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Loaded(
                                    tempC = msg.tempC,
                                    iconUrl = msg.iconUrl
                                )
                            )
                        } else {
                            it
                        }
                    })
                }

                is Msg.WeatherLoading -> {
                    copy(citiItems = citiItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Loading
                            )
                        } else {
                            it
                        }
                    })
                }

                is Msg.WeatherLoadingError -> {
                    copy(citiItems = citiItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Error
                            )
                        } else {
                            it
                        }
                    })
                }
            }

    }
}
