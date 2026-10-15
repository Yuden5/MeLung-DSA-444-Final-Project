package com.example.drukgasapplicationfinalproject2026.components

fun weatherDescription(
    weatherCode: Int
): String {

    return when (weatherCode) {

        0 -> "Clear sky"

        1, 2, 3 ->
            "Partly cloudy"

        45, 48 ->
            "Foggy"

        51, 53, 55,
        56, 57 ->
            "Drizzle"

        61, 63, 65,
        66, 67 ->
            "Rain"

        71, 73, 75,
        77 ->
            "Snow"

        80, 81, 82 ->
            "Rain showers"

        85, 86 ->
            "Snow showers"

        95, 96, 99 ->
            "Thunderstorm"

        else ->
            "Unknown conditions"
    }
}