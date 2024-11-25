package com.example.forecastweather.presenttion.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.entity.Forecast
import com.example.forecastweather.domain.usecase.AddToFavouriteUseCase
import com.example.forecastweather.domain.usecase.GetForecastUseCase
import com.example.forecastweather.domain.usecase.ObserveIsFavouriteStatusUseCase
import com.example.forecastweather.domain.usecase.RemoveFromFavouriteUseCase
import com.example.forecastweather.presenttion.detail.DetailsStore.Intent
import com.example.forecastweather.presenttion.detail.DetailsStore.Label
import com.example.forecastweather.presenttion.detail.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent

        data object ClickChangeFavouriteStatus : Intent
    }

    data class State(
        val isFavourite: Boolean,
        val city: City,
        val forecastState: ForecastState
    ) {
        sealed interface ForecastState {

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Loaded(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {

        data object ClickBack : Label

    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
    private val removeFromFavouriteUseCase: RemoveFromFavouriteUseCase,
    private val observeIsFavouriteStatusUseCase: ObserveIsFavouriteStatusUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                isFavourite = false,
                city = city,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class FavouriteStatusChanged(val isFavourite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastLoading : Action

        data object ForecastError : Action

    }

    private sealed interface Msg {

        data class FavouriteStatusChanged(val isFavourite: Boolean) : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        data object ForecastLoading : Msg

        data object ForecastError : Msg
    }

    private inner class BootstrapperImpl(
        private val city: City
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeIsFavouriteStatusUseCase(city.id).collect {
                    dispatch(Action.FavouriteStatusChanged(it))
                }
            }
            scope.launch {
                dispatch(Action.ForecastLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickChangeFavouriteStatus -> {
                    scope.launch {
                        val state = getState()
                        if (state.isFavourite) {
                            removeFromFavouriteUseCase(state.city.id)
                        } else {
                            addToFavouriteUseCase(state.city)
                        }
                    }
                }
            }

        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteStatusChanged -> {
                    dispatch(Msg.FavouriteStatusChanged(action.isFavourite))
                }

                Action.ForecastError -> {
                    dispatch(Msg.ForecastError)
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                Action.ForecastLoading -> {
                    dispatch(Msg.ForecastLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.FavouriteStatusChanged -> {
                    copy(isFavourite = msg.isFavourite)
                }

                Msg.ForecastError -> {
                    copy(forecastState = State.ForecastState.Error)
                }

                is Msg.ForecastLoaded -> {
                    copy(
                        forecastState = State.ForecastState.Loaded(
                            forecast = msg.forecast
                        )
                    )
                }

                Msg.ForecastLoading -> {
                    copy(forecastState = State.ForecastState.Loading)
                }
            }

    }
}
