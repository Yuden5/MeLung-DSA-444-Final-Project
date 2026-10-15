package com.example.drukgasapplicationfinalproject2026.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    fun loadWeather() {

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                _weather.value =
                    repository.getThimphuWeather()

            } catch (e: Exception) {

                _error.value =
                    "Unable to load weather"

            } finally {

                _isLoading.value = false
            }
        }
    }
}