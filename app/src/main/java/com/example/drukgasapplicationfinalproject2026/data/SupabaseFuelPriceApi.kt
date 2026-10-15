package com.example.drukgasapplicationfinalproject2026.data

import retrofit2.http.GET
import retrofit2.http.Query

interface SupabaseFuelPriceApi {

    @GET("rest/v1/fuel_prices")
    suspend fun getFuelPrices(
        @Query("select")
        select: String =
            "outlet_id,outlet_name,petrol_price,diesel_price,source_date,last_checked_at",

        @Query("order")
        order: String = "outlet_name.asc"
    ): List<FuelPrice>
}