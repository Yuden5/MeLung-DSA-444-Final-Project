package com.example.drukgasapplicationfinalproject2026.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val database = DatabaseProvider.getDatabase(application)
    private val repository = ReportRepository(database.reportDao())

    val reports = repository.allReports
        .map { entities ->
            entities.map { it.toReport() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val latestReports = reports
        .map { reportList ->
            reportList
                .groupBy { it.outletId }
                .mapValues { (_, outletReports) ->
                    outletReports.maxByOrNull { it.reportedAt }
                }
                .filterValues { it != null }
                .mapValues { it.value!! }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    init {
        seedInitialReports()
    }

    fun addReport(report: Report) {
        viewModelScope.launch {
            repository.insertReport(report.toEntity())
        }
    }

    fun deleteReport(reportId: String) {
        viewModelScope.launch {
            repository.deleteReport(reportId)
        }
    }

    fun upvoteReport(reportId: String) {
        viewModelScope.launch {
            repository.upvoteReport(reportId)
        }
    }

    fun addHelpfulScore(reportId: String) {
        viewModelScope.launch {
            repository.addHelpfulScore(reportId)
        }
    }

    private fun seedInitialReports() {
        viewModelScope.launch {
            val count = repository.getReportCount()

            if (count == 0) {
                OutletRepository.reports.forEach { report ->
                    repository.insertReport(report.toEntity())
                }
            }
        }
    }
}
