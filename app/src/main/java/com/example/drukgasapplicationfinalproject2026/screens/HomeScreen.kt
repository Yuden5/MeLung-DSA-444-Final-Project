package com.example.drukgasapplicationfinalproject2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drukgasapplicationfinalproject2026.R
import com.example.drukgasapplicationfinalproject2026.components.OutletCard
import com.example.drukgasapplicationfinalproject2026.data.Availability
import com.example.drukgasapplicationfinalproject2026.data.FuelPrice
import com.example.drukgasapplicationfinalproject2026.data.Outlet
import com.example.drukgasapplicationfinalproject2026.data.OutletRepository
import com.example.drukgasapplicationfinalproject2026.data.QueueLevel
import com.example.drukgasapplicationfinalproject2026.data.Report
import com.example.drukgasapplicationfinalproject2026.data.WeatherResponse
import com.example.drukgasapplicationfinalproject2026.ui.theme.SoftPageBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextDark
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextMuted
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeYellow

@Composable
fun DrukGasHomeScreen(
    modifier: Modifier = Modifier,
    reports: List<Report>,
    latestReports: Map<String, Report>,
    onOutletClick: (Outlet) -> Unit,
    onViewReports: () -> Unit,
    weather: WeatherResponse? = null,
    weatherLoading: Boolean = false,
    weatherError: String? = null,
    fuelPrices: List<FuelPrice>,
    fuelPricesLoading: Boolean,
    fuelPricesError: String?,
    defaultLocation: String = "Thimphu",
    defaultResource: String = "LPG",
    onOpenSettings: () -> Unit = {}
) {
    var selectedResource by remember(defaultResource) { mutableStateOf(defaultResource) }
    var selectedArea by remember(defaultLocation) { mutableStateOf(defaultLocation) }
    var searchQuery by remember { mutableStateOf("") }

    // Feedback dialog states
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var feedbackSenderName by remember { mutableStateOf("") }
    var feedbackMessageText by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Filter states for Availability and Queue
    var showFilterDialog by remember { mutableStateOf(false) }
    var filterAvailability by remember { mutableStateOf<Availability?>(null) }
    var filterQueue by remember { mutableStateOf<QueueLevel?>(null) }

    val focusManager = LocalFocusManager.current

    val allResourceOutlets = OutletRepository.getOutlets(selectedResource)

    // Dzongkhags list
    val availableDzongkhags = remember(selectedResource) {
        if (selectedResource == "Fuel") {
            val fuelDistricts = (OutletRepository.getOutlets("Fuel").map { it.district } + "Paro")
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()
            listOf("All Dzongkhags") + fuelDistricts
        } else {
            listOf("All Dzongkhags", "Thimphu", "Paro", "Bumthang", "Samtse")
        }
    }

    // Filter outlets by Dzongkhag, search, availability, and queue
    val filteredOutlets = allResourceOutlets.filter { outlet ->
        val matchesDistrict = selectedArea.equals("All Dzongkhags", ignoreCase = true) ||
                selectedArea.equals("All dzongkhags", ignoreCase = true) ||
                outlet.district.equals(selectedArea, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                outlet.district.contains(searchQuery, ignoreCase = true) ||
                outlet.name.contains(searchQuery, ignoreCase = true) ||
                outlet.area.contains(searchQuery, ignoreCase = true)

        val report = latestReports[outlet.id]
        val matchesAvail = filterAvailability == null || report?.availability == filterAvailability
        val matchesQueue = filterQueue == null || report?.queueLevel == filterQueue

        matchesDistrict && matchesSearch && matchesAvail && matchesQueue
    }

    val fuelPricesByOutletId = remember(fuelPrices) {
        fuelPrices.associateBy { it.outletId }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftPageBg),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // =====================================================
        // TOP HEADER BAR: CHOSEN LOCATION & SETTINGS BUTTON
        // =====================================================

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Chosen Location Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedArea = "All Dzongkhags" }
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ThemeYellow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Chosen location",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                        Text(
                            text = selectedArea,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }

                // Settings Circle Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE5E7EB), CircleShape)
                        .clickable { onOpenSettings() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // =====================================================
        // SEARCH BAR
        // =====================================================

        item {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search for an outlet",
                        color = Color(0xFF9CA3AF),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFE8ECEF),
                    unfocusedContainerColor = Color(0xFFE8ECEF),
                    disabledContainerColor = Color(0xFFE8ECEF),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )
        }

        // =====================================================
        // AVAILABLE DZONGKHAGS SECTION
        // =====================================================

        item {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Available Dzongkhags",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableDzongkhags) { district ->
                        val isSelected = selectedArea.equals(district, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ThemeBlue else Color.White)
                                .then(
                                    if (!isSelected) Modifier.border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                                    else Modifier
                                )
                                .clickable { selectedArea = district }
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = district,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextDark
                            )
                        }
                    }
                }
            }
        }

        // =====================================================
        // HERO BANNER CARD (new_banner.png)
        // =====================================================

        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // New Banner Image Asset (contains built-in header text)
                    Image(
                        painter = painterResource(id = R.drawable.new_banner),
                        contentDescription = "DrukGas Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Send Feedback Button aligned with banner text
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 84.dp, bottom = 14.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .clickable { showFeedbackDialog = true }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Send Feedback",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }
            }
        }

        // =====================================================
        // RESOURCE CATEGORY (SEGMENTED TOGGLE)
        // =====================================================

        item {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Resource Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Segmented Pill Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE8ECEF))
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // LPG Segment
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedResource == "LPG") ThemeYellow else Color.Transparent)
                                .clickable { selectedResource = "LPG" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "LPG",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedResource == "LPG") Color.White else TextDark
                            )
                        }

                        // Fuel Segment
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedResource == "Fuel") ThemeYellow else Color.Transparent)
                                .clickable { selectedResource = "Fuel" }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Fuel",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedResource == "Fuel") Color.White else TextDark
                            )
                        }
                    }
                }
            }
        }

        // =====================================================
        // RECENT REPORTS HEADER & SMALL FILTER BUTTON
        // =====================================================

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Recent Reports",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark
                )

                // Filter icon button
                IconButton(
                    onClick = { showFilterDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter availability and queue length",
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // =====================================================
        // OUTLET / REPORT CARDS
        // =====================================================

        if (filteredOutlets.isEmpty()) {

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = ThemeBlue,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "No Outlets Found",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "No matching outlets found for \"${searchQuery.ifBlank { selectedArea }}\".",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

        } else {

            items(
                items = filteredOutlets,
                key = { outlet -> outlet.id }
            ) { outlet ->
                OutletCard(
                    outlet = outlet,
                    report = latestReports[outlet.id],
                    fuelPrice = if (outlet.type == "Fuel") {
                        fuelPricesByOutletId[outlet.id]
                    } else {
                        null
                    },
                    onClick = { onOutletClick(outlet) },
                    onViewReports = onViewReports
                )
            }
        }
    }

    // =====================================================
    // FILTER DIALOG FOR AVAILABILITY & QUEUE LENGTH
    // =====================================================

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = {
                Text(
                    text = "Filter Outlets",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Availability Filter Options
                    Text(
                        text = "Availability",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (filterAvailability == null) ThemeBlue else Color(0xFFE5E7EB))
                                .clickable { filterAvailability = null }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("All", color = if (filterAvailability == null) Color.White else TextDark, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (filterAvailability == Availability.AVAILABLE) ThemeBlue else Color(0xFFE5E7EB))
                                .clickable { filterAvailability = Availability.AVAILABLE }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("In-Stock", color = if (filterAvailability == Availability.AVAILABLE) Color.White else TextDark, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (filterAvailability == Availability.OUT_OF_STOCK) ThemeBlue else Color(0xFFE5E7EB))
                                .clickable { filterAvailability = Availability.OUT_OF_STOCK }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Out-of-Stock", color = if (filterAvailability == Availability.OUT_OF_STOCK) Color.White else TextDark, fontSize = 12.sp)
                        }
                    }

                    // Queue Length Filter Options
                    Text(
                        text = "Queue Length",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (filterQueue == null) ThemeBlue else Color(0xFFE5E7EB))
                                .clickable { filterQueue = null }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("All", color = if (filterQueue == null) Color.White else TextDark, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (filterQueue == QueueLevel.SHORT) ThemeBlue else Color(0xFFE5E7EB))
                                .clickable { filterQueue = QueueLevel.SHORT }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Short-Queue", color = if (filterQueue == QueueLevel.SHORT) Color.White else TextDark, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (filterQueue == QueueLevel.LONG) ThemeBlue else Color(0xFFE5E7EB))
                                .clickable { filterQueue = QueueLevel.LONG }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Long-Queue", color = if (filterQueue == QueueLevel.LONG) Color.White else TextDark, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Apply Filter", fontWeight = FontWeight.Bold, color = ThemeBlue)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        filterAvailability = null
                        filterQueue = null
                        showFilterDialog = false
                    }
                ) {
                    Text("Reset", color = Color.Gray)
                }
            }
        )
    }

    // =====================================================
    // FEEDBACK POPUP DIALOG FOR MELUNG APP
    // =====================================================

    if (showFeedbackDialog) {
        AlertDialog(
            onDismissRequest = { showFeedbackDialog = false },
            title = {
                Text(
                    text = "Send Feedback",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Send a message or suggestion directly to the developer.",
                        fontSize = 13.sp,
                        color = TextMuted
                    )

                    // Sender Name
                    Text(text = "Your Name (Optional)", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    OutlinedTextField(
                        value = feedbackSenderName,
                        onValueChange = { feedbackSenderName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Tashi Dorji", color = TextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Feedback Message
                    Text(text = "Feedback Message", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    OutlinedTextField(
                        value = feedbackMessageText,
                        onValueChange = { feedbackMessageText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Write your message or suggestions here...", color = TextMuted) },
                        minLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (feedbackMessageText.isNotBlank()) {
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:yudenkelzang02@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "MeLung App Feedback")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Feedback from: ${feedbackSenderName.ifBlank { "MeLung User" }}\n\nMessage:\n$feedbackMessageText"
                                )
                            }
                            try {
                                context.startActivity(Intent.createChooser(emailIntent, "Send Feedback via Email"))
                            } catch (e: Exception) {
                                // Fallback
                            }
                            showFeedbackDialog = false
                            feedbackSenderName = ""
                            feedbackMessageText = ""
                        }
                    },
                    enabled = feedbackMessageText.isNotBlank()
                ) {
                    Text("Submit Feedback", fontWeight = FontWeight.Bold, color = ThemeBlue)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showFeedbackDialog = false }
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}
