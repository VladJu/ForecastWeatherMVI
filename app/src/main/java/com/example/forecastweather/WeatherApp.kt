package com.example.forecastweather

import android.app.Application
import com.example.forecastweather.di.ApplicationComponent
import com.example.forecastweather.di.DaggerApplicationComponent

class WeatherApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}