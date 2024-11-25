package com.example.forecastweather.data.mapper

import com.example.forecastweather.data.local.db_model.CityDbModel
import com.example.forecastweather.domain.entity.City

fun City.toDbModel(): CityDbModel = CityDbModel(id, name, country)

fun CityDbModel.toEntity(): City = City(id, name, country)

fun List<CityDbModel>.toEntities(): List<City> = map { it.toEntity() }