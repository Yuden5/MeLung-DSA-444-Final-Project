package com.example.drukgasapplicationfinalproject2026.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Insert
    suspend fun insertReport(report: ReportEntity)

    @Query("SELECT * FROM reports ORDER BY reportedAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("""
        SELECT * FROM reports
        WHERE outletId = :outletId
        ORDER BY reportedAt DESC
        LIMIT 1
    """)
    fun getLatestReportForOutlet(outletId: String): Flow<ReportEntity?>

    @Query("SELECT * FROM reports WHERE outletId = :outletId ORDER BY reportedAt DESC")
    fun getReportsForOutlet(outletId: String): Flow<List<ReportEntity>>

    @Query("SELECT COUNT(*) FROM reports")
    suspend fun getReportCount(): Int

    @Query("DELETE FROM reports WHERE id = :reportId")
    suspend fun deleteReportById(reportId: String)

    @Query("UPDATE reports SET accurateUpvotes = accurateUpvotes + 1 WHERE id = :reportId")
    suspend fun upvoteReport(reportId: String)

    @Query("UPDATE reports SET helpfulScores = helpfulScores + 1 WHERE id = :reportId")
    suspend fun addHelpfulScore(reportId: String)

    @Query("DELETE FROM reports")
    suspend fun deleteAllReports()
}
