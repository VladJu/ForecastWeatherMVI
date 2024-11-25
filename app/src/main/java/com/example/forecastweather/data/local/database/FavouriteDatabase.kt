package com.example.forecastweather.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.forecastweather.data.local.db_model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {

    abstract fun favouriteCitiesDao(): FavouriteCitiesDao

    companion object {

        private var INSTANCE: FavouriteDatabase? = null
        private val Lock = Any()
        private const val DB_NAME = "favourite_db"

        fun getInstance(context: Context): FavouriteDatabase {

            INSTANCE?.let {
                return it
            }

            synchronized(Lock) {
                INSTANCE?.let {
                    return it
                }

                val database = Room.databaseBuilder(
                    context,
                    FavouriteDatabase::class.java,
                    DB_NAME
                ).build()

                INSTANCE = database

                return database
            }
        }
    }
}