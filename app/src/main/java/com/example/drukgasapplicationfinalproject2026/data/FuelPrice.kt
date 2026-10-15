package com.example.drukgasapplicationfinalproject2026.data

import com.google.gson.annotations.SerializedName

data class FuelPrice(
    @SerializedName("outlet_id")
    val outletId: String,

    @SerializedName("outlet_name")
    val outletName: String,

    @SerializedName("petrol_price")
    val petrolPrice: Double?,

    @SerializedName("diesel_price")
    val dieselPrice: Double?,

    @SerializedName("source_date")
    val sourceDate: String?,

    @SerializedName("last_checked_at")
    val lastCheckedAt: String?
)