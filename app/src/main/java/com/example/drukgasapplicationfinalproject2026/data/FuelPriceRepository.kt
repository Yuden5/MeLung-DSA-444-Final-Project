package com.example.drukgasapplicationfinalproject2026.data

class FuelPriceRepository {

    suspend fun getFuelPrices(): List<FuelPrice> {
        return SupabaseClient.api.getFuelPrices()
    }
}