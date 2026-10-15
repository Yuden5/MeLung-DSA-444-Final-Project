package com.example.drukgasapplicationfinalproject2026.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drukgasapplicationfinalproject2026.ui.theme.SoftPageBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextDark
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextMuted
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    defaultLocation: String,
    defaultResource: String,
    onUpdateDefaultLocation: (String) -> Unit,
    onUpdateDefaultResource: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var showLocationPicker by remember { mutableStateOf(false) }
    var showResourcePicker by remember { mutableStateOf(false) }
    var showGuidelinesDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val locations = listOf(
        "Thimphu", "Paro", "Bumthang", "Samtse", "Chukha",
        "Dagana", "Sarpang", "Tsirang", "Wangdue Phodrang", "All Dzongkhags"
    )

    val resources = listOf("LPG", "Fuel")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SoftPageBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // TOP HEADER BAR: Back Button & Title "Settings"
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // White Circular Back Button
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onBack() }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextDark,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Title: Settings
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SECTION 1: PREFERENCES
        Text(
            text = "Preferences",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Item 1: Default Location
                SettingRowItem(
                    icon = Icons.Default.LocationOn,
                    title = "Default Location",
                    value = defaultLocation,
                    onClick = { showLocationPicker = true }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color(0xFFF1F5F9)
                )

                // Item 2: Default Resource
                SettingRowItem(
                    icon = Icons.Default.LocalGasStation,
                    title = "Default Resource",
                    value = defaultResource,
                    onClick = { showResourcePicker = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SECTION 2: INFORMATION
        Text(
            text = "Information",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Item 1: Community Guidelines
                SettingRowItem(
                    icon = Icons.Default.Shield,
                    title = "Community Guidelines",
                    value = "",
                    onClick = { showGuidelinesDialog = true }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color(0xFFF1F5F9)
                )

                // Item 2: About MeLung
                SettingRowItem(
                    icon = Icons.Default.Info,
                    title = "About MeLung",
                    value = "",
                    onClick = { showAboutDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // =====================================================
    // DIALOG 1: DEFAULT LOCATION PICKER
    // =====================================================
    if (showLocationPicker) {
        AlertDialog(
            onDismissRequest = { showLocationPicker = false },
            title = {
                Text(text = "Select Default Location", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    locations.forEach { location ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (location == defaultLocation) ThemeBlue.copy(alpha = 0.12f) else Color(0xFFF8FAFC))
                                .clickable {
                                    onUpdateDefaultLocation(location)
                                    showLocationPicker = false
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = location,
                                fontSize = 14.sp,
                                fontWeight = if (location == defaultLocation) FontWeight.Bold else FontWeight.Medium,
                                color = if (location == defaultLocation) ThemeBlue else TextDark
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationPicker = false }) {
                    Text("Close", color = TextMuted)
                }
            }
        )
    }

    // =====================================================
    // DIALOG 2: DEFAULT RESOURCE PICKER
    // =====================================================
    if (showResourcePicker) {
        AlertDialog(
            onDismissRequest = { showResourcePicker = false },
            title = {
                Text(text = "Select Default Resource", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    resources.forEach { resource ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (resource == defaultResource) ThemeBlue.copy(alpha = 0.12f) else Color(0xFFF8FAFC))
                                .clickable {
                                    onUpdateDefaultResource(resource)
                                    showResourcePicker = false
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = resource,
                                fontSize = 14.sp,
                                fontWeight = if (resource == defaultResource) FontWeight.Bold else FontWeight.Medium,
                                color = if (resource == defaultResource) ThemeBlue else TextDark
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showResourcePicker = false }) {
                    Text("Close", color = TextMuted)
                }
            }
        )
    }

    // =====================================================
    // DIALOG 3: COMMUNITY GUIDELINES
    // =====================================================
    if (showGuidelinesDialog) {
        AlertDialog(
            onDismissRequest = { showGuidelinesDialog = false },
            title = {
                Text(
                    text = "DrukGas Community Guidelines",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GuidelineItem("1. Be truthful.", "Share accurate information about LPG and fuel availability.")
                    GuidelineItem("2. Keep reports updated.", "Report conditions you have observed recently.")
                    GuidelineItem("3. Choose the correct location.", "Make sure your report refers to the right outlet or area.")
                    GuidelineItem("4. Be respectful.", "Do not post offensive, abusive, or inappropriate content.")
                    GuidelineItem("5. No fake reports.", "Do not submit misleading information, spam, or intentionally false reports.")
                    GuidelineItem("6. Respect privacy.", "Do not share anyone's personal or sensitive information.")
                    GuidelineItem("7. Verify before relying.", "Community reports may change, so confirm availability with the outlet when necessary.")
                    GuidelineItem("8. Help the community.", "If you notice that a report is outdated or inaccurate, contribute an update when possible.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showGuidelinesDialog = false }) {
                    Text("I Understand", fontWeight = FontWeight.Bold, color = ThemeBlue)
                }
            }
        )
    }

    // =====================================================
    // DIALOG 4: ABOUT MELUNG
    // =====================================================
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(
                    text = "About MeLung",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "MeLung is a mobile application developed as the final project for DSA-444: Mobile App Development.",
                        fontSize = 13.sp,
                        color = TextDark,
                        lineHeight = 18.sp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AboutDetailRow("Developer:", "Kelzang Yuden")
                        AboutDetailRow("Student ID:", "2320210186")
                        AboutDetailRow("Program:", "Final-Year Data Science and AI")
                        AboutDetailRow("Institution:", "Thai-Nichi International College")
                    }

                    Text(
                        text = "MeLung aims to make LPG and fuel availability information more accessible in Bhutan through official price information and community-submitted reports.",
                        fontSize = 13.sp,
                        color = TextDark,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Developer Portfolio:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = "kelscodesign.framer.website",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemeBlue,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kelscodesign.framer.website"))
                            context.startActivity(intent)
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close", fontWeight = FontWeight.Bold, color = ThemeBlue)
                }
            }
        )
    }
}

@Composable
private fun SettingRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEBF3FC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ThemeBlue,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value.isNotBlank()) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = Color(0xFF9CA3AF),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun GuidelineItem(title: String, description: String) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = description,
            fontSize = 12.sp,
            color = TextMuted,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun AboutDetailRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = TextDark
        )
    }
}
