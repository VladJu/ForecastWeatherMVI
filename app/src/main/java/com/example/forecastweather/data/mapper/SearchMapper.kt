package com.example.forecastweather.data.mapper

import com.example.forecastweather.data.remote.dto.CityDto
import com.example.forecastweather.domain.entity.City

fun CityDto.toEntity(): City = City(id, name, country)

fun List<CityDto>.toEntities():List<City> = map { it.toEntity() }