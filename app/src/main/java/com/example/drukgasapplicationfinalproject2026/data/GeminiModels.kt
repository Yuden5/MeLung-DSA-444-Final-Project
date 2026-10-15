package com.example.drukgasapplicationfinalproject2026.data

data class GeminiRequest(
    val contents: List<GeminiContent>
)

data class GeminiContent(
    val role: String = "user",
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

data class GeminiCandidate(
    val content: GeminiContentResponse?
)

data class GeminiContentResponse(
    val parts: List<GeminiPart>?
)