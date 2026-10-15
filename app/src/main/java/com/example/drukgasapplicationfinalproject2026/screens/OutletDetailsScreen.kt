package com.example.drukgasapplicationfinalproject2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drukgasapplicationfinalproject2026.data.Availability
import com.example.drukgasapplicationfinalproject2026.data.Outlet
import com.example.drukgasapplicationfinalproject2026.data.QueueLevel
import com.example.drukgasapplicationfinalproject2026.data.Report
import com.example.drukgasapplicationfinalproject2026.data.formatReportTime
import com.example.drukgasapplicationfinalproject2026.ui.theme.SoftPageBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusInStockBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusInStockDot
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusInStockFg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusLowBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusLowDot
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusLowFg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusOutOfStockBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusOutOfStockDot
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusOutOfStockFg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusUnknownBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusUnknownDot
import com.example.drukgasapplicationfinalproject2026.ui.theme.StatusUnknownFg
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextMuted
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextNavy
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeRed

@Composable
fun OutletDetailsScreen(
    modifier: Modifier = Modifier,
    outlet: Outlet,
    reports: List<Report>,
    onBack: () -> Unit
) {
    val outletReports = reports.filter { it.outletId == outlet.id }.sortedByDescending { it.reportedAt }
    val latestReport = outletReports.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftPageBg)
            .verticalScroll(rememberScrollState())
    ) {
        // HERO HEADER (#293f76, #b22e2f, #fdeea8)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = ThemeBlue,
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = outlet.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = ThemeRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${outlet.area} · ${outlet.district} · ${outlet.operator}",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Current Status",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavy
            )

            if (latestReport == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "No Recent Report",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No community update available for this outlet.",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }
            } else {
                StatusCard(report = latestReport)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Report History",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextNavy
            )

            if (outletReports.isEmpty()) {
                Text(
                    text = "No report history recorded yet.",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            } else {
                outletReports.forEach { report ->
                    OutletReportHistoryCard(report = report)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun StatusCard(report: Report) {
    val (availText, availBg, availFg, availDot) = when (report.availability) {
        Availability.AVAILABLE -> Quad("In stock", StatusInStockBg, StatusInStockFg, StatusInStockDot)
        Availability.LOW -> Quad("Running low", StatusLowBg, StatusLowFg, StatusLowDot)
        Availability.OUT_OF_STOCK -> Quad("Out of stock", StatusOutOfStockBg, StatusOutOfStockFg, StatusOutOfStockDot)
        Availability.UNKNOWN -> Quad("Unknown", StatusUnknownBg, StatusUnknownFg, StatusUnknownDot)
    }

    val (queueText, queueBg, queueFg, queueDot) = when (report.queueLevel) {
        QueueLevel.SHORT -> Quad("Short queue", StatusInStockBg, StatusInStockFg, StatusInStockDot)
        QueueLevel.MODERATE -> Quad("Moderate queue", StatusLowBg, StatusLowFg, StatusLowDot)
        QueueLevel.LONG -> Quad("Long queue", StatusOutOfStockBg, StatusOutOfStockFg, StatusOutOfStockDot)
        QueueLevel.UNKNOWN -> Quad("Queue unknown", StatusUnknownBg, StatusUnknownFg, StatusUnknownDot)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = availBg
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Availability", fontSize = 10.sp, color = availFg.copy(alpha = 0.8f), fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(availDot, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(availText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = availFg)
                        }
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = queueBg
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Queue Length", fontSize = 10.sp, color = queueFg.copy(alpha = 0.8f), fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(queueDot, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(queueText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = queueFg)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Updated ${formatReportTime(report.reportedAt)}",
                fontSize = 12.sp,
                color = TextMuted
            )

            if (report.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "“${report.note}”",
                    fontSize = 13.sp,
                    color = TextNavy,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun OutletReportHistoryCard(report: Report) {
    val (availText, _, availFg, availDot) = when (report.availability) {
        Availability.AVAILABLE -> Quad("In stock", StatusInStockBg, StatusInStockFg, StatusInStockDot)
        Availability.LOW -> Quad("Running low", StatusLowBg, StatusLowFg, StatusLowDot)
        Availability.OUT_OF_STOCK -> Quad("Out of stock", StatusOutOfStockBg, StatusOutOfStockFg, StatusOutOfStockDot)
        Availability.UNKNOWN -> Quad("Unknown", StatusUnknownBg, StatusUnknownFg, StatusUnknownDot)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(availDot, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(availText, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = availFg)
                }
                Text(formatReportTime(report.reportedAt), fontSize = 12.sp, color = TextMuted)
            }

            if (report.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(report.note, fontSize = 12.sp, color = TextNavy)
            }
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
