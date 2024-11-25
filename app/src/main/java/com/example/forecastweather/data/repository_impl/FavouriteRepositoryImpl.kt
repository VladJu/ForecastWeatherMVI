package com.example.forecastweather.data.repository_impl

import com.example.forecastweather.data.local.database.FavouriteCitiesDao
import com.example.forecastweather.data.mapper.toDbModel
import com.example.forecastweather.data.mapper.toEntities
import com.example.forecastweather.domain.entity.City
import com.example.forecastweather.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteCitiesDao: FavouriteCitiesDao
) : FavouriteRepository {
    override suspend fun addToFavourite(city: City) {
        favouriteCitiesDao.addToFavourite(city.toDbModel())
    }

    override val favouriteCities: Flow<List<City>>
        get() = favouriteCitiesDao.getFavoriteCities().map { it.toEntities() }

    override fun observeIsFavourite(cityId: Int): Flow<Boolean> {
        return favouriteCitiesDao.observeIsFavourite(cityId)
    }

    override suspend fun removeFromFavourite(cityId: Int) {
        favouriteCitiesDao.removeFromFavourite(cityId)
    }
}