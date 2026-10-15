package com.example.drukgasapplicationfinalproject2026.data

class WeatherRepository {

    private val api = WeatherApiClient.service

    suspend fun getThimphuWeather(): WeatherResponse {
        return api.getCurrentWeather(
            latitude = 27.4728,
            longitude = 89.6393
        )
    }
}