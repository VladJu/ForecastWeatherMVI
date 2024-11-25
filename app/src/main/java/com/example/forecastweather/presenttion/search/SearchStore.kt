package com.example.forecastweather.presenttion.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.usecase.AddToFavouriteUseCase
import com.example.forecastweather.domain.usecase.SearchCityUseCase
import com.example.forecastweather.presenttion.search.SearchStore.Intent
import com.example.forecastweather.presenttion.search.SearchStore.Label
import com.example.forecastweather.presenttion.search.SearchStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeQueryResult(val query: String) : Intent

        data class ClickCity(val city: City) : Intent

        data object ClickBack : Intent

        data object ClickSearch : Intent

    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {
            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object EmptyResult : SearchState

            data class Loaded(val cities: List<City>) : SearchState

        }
    }

    sealed interface Label {
        data object SavedToFavourite : Label

        data object ClickBack : Label

        data class OpenForecast(val city: City) : Label

    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
    private val searchCityUseCase: SearchCityUseCase
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {}

    private sealed interface Msg {

        data class ChangeSearchQuery(val query: String) : Msg

        data object LoadingSearchResult : Msg

        data object SearchResultError : Msg

        data class SearchResultLoaded(val cities: List<City>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {}
    }

    private inner class ExecutorImpl(
        private val openReason: OpenReason
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeQueryResult -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.AddToFavourite -> {
                            scope.launch {
                                addToFavouriteUseCase(intent.city)
                                publish(Label.SavedToFavourite)
                            }
                        }

                        OpenReason.RegularSearch -> {
                            publish(Label.OpenForecast(intent.city))
                        }
                    }
                }

                Intent.ClickSearch -> {
                    dispatch(Msg.LoadingSearchResult)
                    scope.launch {
                        try {
                            val cities = searchCityUseCase(getState().searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.SearchResultError)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeSearchQuery -> {
                    copy(searchQuery = msg.query)
                }

                Msg.LoadingSearchResult -> {
                    copy(searchState = State.SearchState.Loading)
                }

                Msg.SearchResultError -> {
                    copy(searchState = State.SearchState.Error)

                }

                is Msg.SearchResultLoaded -> {
                    val searchState = if (msg.cities.isEmpty()) {
                        State.SearchState.EmptyResult
                    } else {
                        State.SearchState.Loaded(
                            msg.cities
                        )
                    }
                    copy(searchState = searchState)

                }
            }

    }
}
