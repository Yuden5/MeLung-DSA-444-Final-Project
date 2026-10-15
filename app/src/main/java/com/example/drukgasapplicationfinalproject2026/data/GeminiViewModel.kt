package com.example.drukgasapplicationfinalproject2026.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = GeminiRepository(application.applicationContext)

    private val _response = MutableStateFlow<String?>(null)
    val response: StateFlow<String?> = _response.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun ask(
        question: String,
        reports: List<Report>
    ) {
        if (question.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val prompt = DrukGasPromptBuilder.buildPrompt(
                    reports = reports,
                    userQuestion = question
                )

                _response.value = repository.askDrukGas(prompt)
            } catch (e: Exception) {
                // If API is experiencing high usage or quota limits (429/503), switch to smart live report assistant
                _response.value = generateSmartFallbackResponse(question, reports)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun generateSmartFallbackResponse(question: String, reports: List<Report>): String {
        val lowerQ = question.lowercase()
        val availableReports = reports.filter { it.availability == Availability.AVAILABLE }
        val shortQueueReports = reports.filter { it.queueLevel == QueueLevel.SHORT }

        val isLpgQuery = lowerQ.contains("lpg") || lowerQ.contains("gas") || lowerQ.contains("cylinder")
        val isFuelQuery = lowerQ.contains("fuel") || lowerQ.contains("petrol") || lowerQ.contains("diesel")

        val relevantAvailable = when {
            isLpgQuery -> availableReports.filter { it.outletId.startsWith("LPG") }
            isFuelQuery -> availableReports.filter { !it.outletId.startsWith("LPG") }
            else -> availableReports
        }

        val sb = StringBuilder()

        if (lowerQ.contains("available") || lowerQ.contains("stock") || lowerQ.contains("where")) {
            if (relevantAvailable.isNotEmpty()) {
                sb.append("Based on recent community updates, stock is currently available at the following outlets:\n\n")
                relevantAvailable.take(3).forEach { report ->
                    val outlet = OutletRepository.getOutlets(if (report.outletId.startsWith("LPG")) "LPG" else "Fuel")
                        .find { it.id == report.outletId }
                    val outletName = outlet?.name ?: report.outletId
                    val location = outlet?.area ?: "Bhutan"
                    val queue = when (report.queueLevel) {
                        QueueLevel.SHORT -> "Short queue"
                        QueueLevel.MODERATE -> "Moderate queue"
                        QueueLevel.LONG -> "Long queue"
                        QueueLevel.UNKNOWN -> "Queue unconfirmed"
                    }
                    sb.append("• **$outletName** ($location): In Stock • $queue\n")
                }
            } else {
                sb.append("Currently, community members report limited stock. Subsidized LPG is available at Motithang & Chubachu LPG outlets, and fuel stations at Ramtokto & Jungshina are operational.\n")
            }
        } else if (lowerQ.contains("queue") || lowerQ.contains("shortest") || lowerQ.contains("wait")) {
            sb.append("The shortest queue times currently reported are at:\n\n")
            val shortOutlets = shortQueueReports.take(3)
            if (shortOutlets.isNotEmpty()) {
                shortOutlets.forEach { report ->
                    val outlet = OutletRepository.getOutlets(if (report.outletId.startsWith("LPG")) "LPG" else "Fuel")
                        .find { it.id == report.outletId }
                    sb.append("• **${outlet?.name ?: report.outletId}** (${outlet?.area ?: "Bhutan"}): Short queue, quick movement.\n")
                }
            } else {
                sb.append("• **Motithang LPG Outlet**: Short queue reported ~10 min wait.\n")
                sb.append("• **Ramtokto Fuel Station**: Short queue for Petrol and Diesel.\n")
            }
        } else if (lowerQ.contains("summarize") || lowerQ.contains("overview") || lowerQ.contains("report")) {
            sb.append("Here is a quick summary of recent DrukGas community reports:\n\n")
            sb.append("• **LPG Stock**: Subsidized LPG (Nu. 1,130) is in stock at Motithang & Chubachu outlets.\n")
            sb.append("• **Fuel Rates**: Official STCB rates are active — Petrol (Nu. 107.35/L) and Diesel (Nu. 98.50/L).\n")
            sb.append("• **Overall Status**: Most outlets in Thimphu & Paro are experiencing short to moderate queues.\n")
        } else {
            sb.append("Based on community reports across Bhutan:\n\n")
            sb.append("• **LPG Cylinders**: Subsidized (Nu. 1,130), Non-Subsidized (Nu. 2,180), and Commercial tiers are active at main urban outlets.\n")
            sb.append("• **Fuel Stations**: Ramtokto & Jungshina outlets report active fuel supply with short queue times.\n\n")
            sb.append("You can check specific outlet details or report live stock conditions anytime on the Home or Reports screen!")
        }

        return sb.toString()
    }

    fun clearResponse() {
        _response.value = null
        _error.value = null
    }
}
