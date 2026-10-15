package com.example.drukgasapplicationfinalproject2026.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeminiApiClient {

    private const val BASE_URL =
        "https://generativelanguage.googleapis.com/"

    val service: GeminiApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(GeminiApiService::class.java)
    }
}