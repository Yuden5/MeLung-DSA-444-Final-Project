package com.example.drukgasapplicationfinalproject2026.data

import com.example.drukgasapplicationfinalproject2026.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
                .addHeader(
                    "Authorization",
                    "Bearer ${BuildConfig.SUPABASE_ANON_KEY}"
                )
                .build()

            chain.proceed(request)
        }
        .build()

    val api: SupabaseFuelPriceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL.trimEnd('/') + "/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseFuelPriceApi::class.java)
    }
}