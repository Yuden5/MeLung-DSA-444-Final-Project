
package com.example.drukgasapplicationfinalproject2026.data

data class Outlet(
    val id: String,
    val name: String,
    val area: String,
    val operator: String,
    val type: String,
    val district: String = "Thimphu"
)