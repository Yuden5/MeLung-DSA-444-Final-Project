package com.example.drukgasapplicationfinalproject2026.data

import android.content.Context

class GeminiRepository(
    context: Context
) {

    private val api = GeminiApiClient.service

    private val apiKey: String =
        context.packageManager
            .getApplicationInfo(
                context.packageName,
                android.content.pm.PackageManager.GET_META_DATA
            )
            .metaData
            ?.getString("GEMINI_API_KEY")
            ?: ""

    suspend fun askDrukGas(prompt: String): String {

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(
                        GeminiPart(text = prompt)
                    )
                )
            )
        )

        val response = api.generateContent(
            apiKey = apiKey,
            request = request
        )

        return response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: "I couldn't generate a response right now."
    }
}