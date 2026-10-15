package com.example.drukgasapplicationfinalproject2026.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey
    val id: String,

    val outletId: String,

    val availability: String,

    val queueLevel: String,

    val reportedAt: Long,

    val note: String,

    val imageUri: String? = null,

    val userName: String? = "Kiki Eden",

    val accurateUpvotes: Int = 4,

    val helpfulScores: Int = 5
)
