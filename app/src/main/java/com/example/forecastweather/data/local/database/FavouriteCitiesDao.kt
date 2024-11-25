package com.example.forecastweather.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastweather.data.local.db_model.CityDbModel
import com.example.forecastweather.domain.entity.City
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCitiesDao {

    @Query("SELECT * FROM favourite_cities")
    fun getFavoriteCities():Flow<List<CityDbModel>>

    @Query("SELECT EXISTS(SELECT * FROM favourite_cities WHERE id=:cityId LIMIT 1)")
    fun observeIsFavourite(cityId: Int) : Flow<Boolean>

    @Query("DELETE FROM favourite_cities WHERE id=:cityId")
    suspend fun removeFromFavourite(cityId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun addToFavourite(cityDb: CityDbModel)
}