package com.example.drukgasapplicationfinalproject2026.data

data class Report(
    val id: String,
    val outletId: String,
    val availability: Availability,
    val queueLevel: QueueLevel,
    val reportedAt: Long,
    val note: String = "",
    val imageUri: String? = null,
    val userName: String = "Kiki Eden",
    val accurateUpvotes: Int = 4,
    val helpfulScores: Int = 5
)

enum class Availability {
    AVAILABLE,
    LOW,
    OUT_OF_STOCK,
    UNKNOWN
}

enum class QueueLevel {
    SHORT,
    MODERATE,
    LONG,
    UNKNOWN
}

fun Report.toEntity(): ReportEntity {
    return ReportEntity(
        id = id,
        outletId = outletId,
        availability = availability.name,
        queueLevel = queueLevel.name,
        reportedAt = reportedAt,
        note = note,
        imageUri = imageUri,
        userName = userName,
        accurateUpvotes = accurateUpvotes,
        helpfulScores = helpfulScores
    )
}

fun ReportEntity.toReport(): Report {
    return Report(
        id = id,
        outletId = outletId,
        availability = Availability.valueOf(availability),
        queueLevel = QueueLevel.valueOf(queueLevel),
        reportedAt = reportedAt,
        note = note,
        imageUri = imageUri,
        userName = userName ?: "Kiki Eden",
        accurateUpvotes = accurateUpvotes,
        helpfulScores = helpfulScores
    )
}
