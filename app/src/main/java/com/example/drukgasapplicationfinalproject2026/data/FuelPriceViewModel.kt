
package com.example.drukgasapplicationfinalproject2026.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class FuelPriceViewModel : ViewModel() {

    private val repository = FuelPriceRepository()

    private val _fuelPrices =
        MutableStateFlow<List<FuelPrice>>(emptyList())
    val fuelPrices: StateFlow<List<FuelPrice>> =
        _fuelPrices.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchFuelPrices()
    }

    fun fetchFuelPrices() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null


            try {
                val prices = repository.getFuelPrices()
                _fuelPrices.value = prices

                Log.d(
                    "FuelPriceDebug",
                    "Successfully fetched ${prices.size} fuel price records"
                )

                prices.forEach { price ->
                    Log.d(
                        "FuelPriceDebug",
                        "ID=${price.outletId}, " +
                                "name=${price.outletName}, " +
                                "petrol=${price.petrolPrice}, " +
                                "diesel=${price.dieselPrice}"
                    )
                }

            } catch (e: Exception) {
                val errorDetails = Log.getStackTraceString(e)

                _error.value = errorDetails

                Log.e("FuelPriceDebug", "ERROR TYPE: ${e.javaClass.name}")
                Log.e("FuelPriceDebug", "ERROR MESSAGE: ${e.message}")
                Log.e("FuelPriceDebug", errorDetails)
            } finally {
                _loading.value = false
            }
        }
    }
}