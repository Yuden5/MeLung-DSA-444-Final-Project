package com.example.drukgasapplicationfinalproject2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.drukgasapplicationfinalproject2026.screens.DrukGasApp
import com.example.drukgasapplicationfinalproject2026.ui.theme.DrukGasApplicationFinalProject2026Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DrukGasApplicationFinalProject2026Theme() {
                DrukGasApp()
            }
        }
    }
}