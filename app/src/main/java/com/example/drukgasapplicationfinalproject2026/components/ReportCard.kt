package com.example.drukgasapplicationfinalproject2026.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.drukgasapplicationfinalproject2026.R
import com.example.drukgasapplicationfinalproject2026.data.Availability
import com.example.drukgasapplicationfinalproject2026.data.OutletRepository
import com.example.drukgasapplicationfinalproject2026.data.QueueLevel
import com.example.drukgasapplicationfinalproject2026.data.Report
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextDark
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextMuted
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeYellow

private fun formatReportTime(reportedAt: Long): String {
    val now = System.currentTimeMillis()
    val elapsed = (now - reportedAt).coerceAtLeast(0L)

    val minutes = elapsed / 60_000L
    val hours = elapsed / 3_600_000L
    val days = elapsed / 86_400_000L

    return when {
        minutes < 1 -> "Updated just now"
        minutes < 60 -> "Updated $minutes minutes"
        hours < 24 -> "Updated $hours hours"
        days == 1L -> "Updated yesterday"
        else -> "Updated $days days"
    }
}

@Composable
fun ReportCard(
    report: Report,
    onUpvote: () -> Unit = {},
    onHelpful: () -> Unit = {}
) {
    val outlet = OutletRepository.getOutlets(
        if (report.outletId.startsWith("LPG")) "LPG" else "Fuel"
    ).find { it.id == report.outletId }

    val isLpg = report.outletId.startsWith("LPG")

    var isUpvoted by remember { mutableStateOf(false) }
    var upvoteCount by remember { mutableIntStateOf(report.accurateUpvotes) }

    var isHearted by remember { mutableStateOf(false) }
    var helpfulCount by remember { mutableIntStateOf(report.helpfulScores) }

    val availText = when (report.availability) {
        Availability.AVAILABLE -> "In-Stock"
        Availability.LOW -> "Running-Low"
        Availability.OUT_OF_STOCK -> "Out-of-Stock"
        Availability.UNKNOWN -> "Unknown"
    }

    val queueText = when (report.queueLevel) {
        QueueLevel.SHORT -> "Short"
        QueueLevel.MODERATE -> "Moderate"
        QueueLevel.LONG -> "Long"
        QueueLevel.UNKNOWN -> "Unknown"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // GOLD YELLOW CATEGORY BADGE (LPG / FUEL)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ThemeYellow)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isLpg) "LPG" else "Fuel",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // OUTLET NAME, OPERATOR, & UPDATED TIME
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(
                        text = outlet?.name ?: "Motithang Fuel Outlet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Operator: ${outlet?.operator ?: "STCBL"}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Text(
                    text = formatReportTime(report.reportedAt),
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // USER PROFILE ROW
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = report.userName.ifBlank { "User Name" },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // USER NOTE TEXT
            Text(
                text = report.note.ifBlank {
                    "I reached here at 13:00 in the afternoon and there wasn't alot of people lining up for queue. Got my gas in around 10 minutes."
                },
                fontSize = 13.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ATTACHED PHOTO IMAGE (ONLY RENDER IF IMAGE IS PRESENT)
            val imageModel: Any? = when {
                report.imageUri == "report1" -> R.drawable.report1
                report.imageUri == "report2" -> R.drawable.report2
                report.imageUri == "report3" -> R.drawable.report3
                report.imageUri?.isNotBlank() == true -> report.imageUri
                else -> null
            }

            if (imageModel != null) {
                AsyncImage(
                    model = imageModel,
                    contentDescription = "Report photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // STATUS GRID (Availability & Queue length)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Availability Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = ThemeBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Availability",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Text(
                        text = availText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                // Queue length Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = ThemeBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Queue length",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Text(
                        text = queueText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(thickness = 1.dp, color = Color(0xFFF1F5F9))

            Spacer(modifier = Modifier.height(10.dp))

            // INTERACTIVE UPVOTE & HEART ICONS AT THE END OF THE CARD
            // Blue by default, turns Yellow when clicked/interacted with
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Verify Report",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Accuracy Checkmark/Upvote Icon Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                isUpvoted = !isUpvoted
                                upvoteCount += if (isUpvoted) 1 else -1
                                onUpvote()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Upvote for accuracy",
                            tint = if (isUpvoted) ThemeYellow else ThemeBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$upvoteCount",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUpvoted) ThemeYellow else ThemeBlue
                        )
                    }

                    // Helpful Heart Icon Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                isHearted = !isHearted
                                helpfulCount += if (isHearted) 1 else -1
                                onHelpful()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isHearted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Heart for helpful score",
                            tint = if (isHearted) ThemeYellow else ThemeBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$helpfulCount",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHearted) ThemeYellow else ThemeBlue
                        )
                    }
                }
            }
        }
    }
}
