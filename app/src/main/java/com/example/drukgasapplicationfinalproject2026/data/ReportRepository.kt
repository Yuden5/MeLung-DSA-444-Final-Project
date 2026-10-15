package com.example.drukgasapplicationfinalproject2026.data

import kotlinx.coroutines.flow.Flow

class ReportRepository(
    private val reportDao: ReportDao
) {

    val allReports: Flow<List<ReportEntity>> =
        reportDao.getAllReports()

    suspend fun insertReport(report: ReportEntity) {
        reportDao.insertReport(report)
    }

    suspend fun deleteReport(reportId: String) {
        reportDao.deleteReportById(reportId)
    }

    suspend fun upvoteReport(reportId: String) {
        reportDao.upvoteReport(reportId)
    }

    suspend fun addHelpfulScore(reportId: String) {
        reportDao.addHelpfulScore(reportId)
    }

    fun getLatestReportForOutlet(outletId: String): Flow<ReportEntity?> {
        return reportDao.getLatestReportForOutlet(outletId)
    }

    fun getReportsForOutlet(outletId: String): Flow<List<ReportEntity>> {
        return reportDao.getReportsForOutlet(outletId)
    }

    suspend fun getReportCount(): Int {
        return reportDao.getReportCount()
    }
}
