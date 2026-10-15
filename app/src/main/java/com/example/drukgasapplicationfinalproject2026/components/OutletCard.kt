package com.example.drukgasapplicationfinalproject2026.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.drukgasapplicationfinalproject2026.R
import com.example.drukgasapplicationfinalproject2026.data.Availability
import com.example.drukgasapplicationfinalproject2026.data.FuelPrice
import com.example.drukgasapplicationfinalproject2026.data.Outlet
import com.example.drukgasapplicationfinalproject2026.data.QueueLevel
import com.example.drukgasapplicationfinalproject2026.data.Report
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusAmber
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusGreen
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusRed
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextDark
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextMuted
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeLightBlue
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
fun OutletCard(
    outlet: Outlet,
    report: Report?,
    onClick: () -> Unit,
    onViewReports: () -> Unit,
    fuelPrice: FuelPrice? = null
) {
    val isLpg = outlet.type == "LPG"

    // Expandable card state
    var isExpanded by remember { mutableStateOf(false) }

    // Interactive upvote & heart state inside expanded view
    var isUpvoted by remember { mutableStateOf(false) }
    var upvoteCount by remember(report) { mutableIntStateOf(report?.accurateUpvotes ?: 4) }

    var isHearted by remember { mutableStateOf(false) }
    var helpfulCount by remember(report) { mutableIntStateOf(report?.helpfulScores ?: 5) }

    // For Fuel: "P" or "D"
    var selectedFuelType by remember { mutableStateOf("P") }
    // For LPG: "S" (Subsidized), "NS" (Non-Subsidized), "C" (Commercial)
    var selectedLpgTier by remember { mutableStateOf("S") }

    val (availText, availColor) = when (report?.availability) {
        Availability.AVAILABLE -> "In-Stock" to StatusGreen
        Availability.LOW -> "Running-Low" to StatusAmber
        Availability.OUT_OF_STOCK -> "Out-of-Stock" to StatusRed
        Availability.UNKNOWN, null -> "In-Stock" to StatusGreen
    }

    val (queueText, queueColor) = when (report?.queueLevel) {
        QueueLevel.SHORT -> "Short-Queue" to StatusGreen
        QueueLevel.MODERATE -> "Moderate-Queue" to StatusAmber
        QueueLevel.LONG -> "Long-Queue" to StatusRed
        QueueLevel.UNKNOWN, null -> "Long-Queue" to StatusRed
    }

    Card(
        onClick = { isExpanded = !isExpanded },
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
            modifier = Modifier.fillMaxWidth()
        ) {
            // TOP FLOATING TIME BADGE & TOP-RIGHT CATEGORY BADGES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // FLOATING TIME BADGE (Top-Left)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 20.dp, bottomEnd = 16.dp))
                        .background(ThemeBlue)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (report != null) formatReportTime(report.reportedAt) else "Updated 20 minutes",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // CATEGORY BADGES (Top-Right)
                Row(
                    modifier = Modifier.padding(top = 12.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isLpg) {
                        // S = Subsidized (#2355CA Blue when clicked/selected)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (selectedLpgTier == "S") Color(0xFF2355CA) else ThemeLightBlue)
                                .clickable { selectedLpgTier = "S" },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (selectedLpgTier == "S") Color.White else Color(0xFF2355CA)
                            )
                        }

                        // NS = Non-Subsidized (#2C9562 Green when clicked/selected)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (selectedLpgTier == "NS") Color(0xFF2C9562) else Color(0xFFD1FAE5))
                                .clickable { selectedLpgTier = "NS" },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (selectedLpgTier == "NS") Color.White else Color(0xFF2C9562)
                            )
                        }

                        // C = Commercial (White)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.5.dp, TextDark, CircleShape)
                                .clickable { selectedLpgTier = "C" },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "C",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                        }
                    } else {
                        // P = Petrol (Yellow)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (selectedFuelType == "P") ThemeYellow else ThemeLightBlue)
                                .clickable { selectedFuelType = "P" },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "P",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (selectedFuelType == "P") Color.White else ThemeBlue
                            )
                        }

                        // D = Diesel (Light Blue)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (selectedFuelType == "D") ThemeYellow else ThemeLightBlue)
                                .clickable { selectedFuelType = "D" },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "D",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (selectedFuelType == "D") Color.White else ThemeBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // MAIN CONTENT BODY
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
            ) {
                // OUTLET NAME & OPERATOR
                Text(
                    text = outlet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Operator: ${outlet.operator}",
                    fontSize = 12.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                // DUMMY PRICE DISPLAY (LPG Tiers vs Fuel)
                val priceText = if (isLpg) {
                    when (selectedLpgTier) {
                        "S" -> "Nu. 1,130"
                        "NS" -> "Nu. 2,180"
                        "C" -> "Nu. 3,450"
                        else -> "Nu. 1,130"
                    }
                } else {
                    if (selectedFuelType == "P") {
                        fuelPrice?.petrolPrice?.let { "Nu. %.2f/ Ltr".format(it) } ?: "Nu. 107.35/ Ltr"
                    } else {
                        fuelPrice?.dieselPrice?.let { "Nu. %.2f/ Ltr".format(it) } ?: "Nu. 98.50/ Ltr"
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = priceText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )

                    if (isLpg) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (selectedLpgTier) {
                                "S" -> "(Subsidized)"
                                "NS" -> "(Non-Subsidized)"
                                "C" -> "(Commercial Rate)"
                                else -> ""
                            },
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // DUAL STATUS PROGRESS BARS (In-Stock & Queue)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Availability Status Bar
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(availColor)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = availText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark
                        )
                    }

                    // Queue Length Status Bar
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(queueColor)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = queueText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // BOTTOM NOTE ROW & EXPAND ICON
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Notes,
                            contentDescription = null,
                            tint = ThemeBlue,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = report?.note?.takeIf { it.isNotBlank() }?.let { "“$it”" }
                                ?: "“Not alot of people at the outlet....”",
                            fontSize = 12.sp,
                            color = TextMuted,
                            maxLines = if (isExpanded) 10 else 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Expand / Collapse Chevron Icon
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand Report",
                        tint = ThemeBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // EXPANDED REPORT DETAILS INLINE
                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        HorizontalDivider(thickness = 1.dp, color = Color(0xFFF1F5F9))

                        Spacer(modifier = Modifier.height(12.dp))

                        // Submitter User Profile Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "User Avatar",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Reported by ${report?.userName ?: "Sonam345"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Attached Photo Image (Render only if image is present)
                        val imageModel: Any? = when {
                            report?.imageUri == "report1" -> R.drawable.report1
                            report?.imageUri == "report2" -> R.drawable.report2
                            report?.imageUri == "report3" -> R.drawable.report3
                            report?.imageUri?.isNotBlank() == true -> report.imageUri
                            else -> null
                        }

                        if (imageModel != null) {
                            AsyncImage(
                                model = imageModel,
                                contentDescription = "Report Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Interactive Verification Upvote & Heart Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Verify Report:",
                                fontSize = 11.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Upvote Checkmark
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            isUpvoted = !isUpvoted
                                            upvoteCount += if (isUpvoted) 1 else -1
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Upvote",
                                        tint = if (isUpvoted) ThemeYellow else ThemeBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$upvoteCount Upvotes",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUpvoted) ThemeYellow else ThemeBlue
                                    )
                                }

                                // Helpful Heart
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            isHearted = !isHearted
                                            helpfulCount += if (isHearted) 1 else -1
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isHearted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Helpful",
                                        tint = if (isHearted) ThemeYellow else ThemeBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$helpfulCount Helpful",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isHearted) ThemeYellow else ThemeBlue
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
