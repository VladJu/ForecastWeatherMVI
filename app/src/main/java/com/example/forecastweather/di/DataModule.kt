package com.example.forecastweather.di

import android.content.Context
import com.example.forecastweather.data.local.database.FavouriteCitiesDao
import com.example.forecastweather.data.local.database.FavouriteDatabase
import com.example.forecastweather.data.remote.api.ApiFactory
import com.example.forecastweather.data.remote.api.ApiService
import com.example.forecastweather.data.repository_impl.FavouriteRepositoryImpl
import com.example.forecastweather.data.repository_impl.SearchRepositoryImpl
import com.example.forecastweather.data.repository_impl.WeatherRepositoryImpl
import com.example.forecastweather.domain.repository.FavouriteRepository
import com.example.forecastweather.domain.repository.SearchRepository
import com.example.forecastweather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.apiService

        @[ApplicationScope Provides]
        fun provideFavouriteDatabase(context: Context): FavouriteDatabase {
            return FavouriteDatabase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideFavouriteCitiesDao(db: FavouriteDatabase): FavouriteCitiesDao {
            return db.favouriteCitiesDao()
        }

    }
}