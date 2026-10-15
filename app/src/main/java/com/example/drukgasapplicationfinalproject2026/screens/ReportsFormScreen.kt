package com.example.drukgasapplicationfinalproject2026.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.drukgasapplicationfinalproject2026.components.ChoiceCard
import com.example.drukgasapplicationfinalproject2026.components.SimpleDropdown
import com.example.drukgasapplicationfinalproject2026.data.Availability
import com.example.drukgasapplicationfinalproject2026.data.Outlet
import com.example.drukgasapplicationfinalproject2026.data.OutletRepository
import com.example.drukgasapplicationfinalproject2026.data.QueueLevel
import com.example.drukgasapplicationfinalproject2026.data.Report
import com.example.drukgasapplicationfinalproject2026.ui.theme.SoftPageBg
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextDark
import com.example.drukgasapplicationfinalproject2026.ui.theme.TextMuted
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeLightBlue
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeYellow

@Composable
fun ReportFormScreen(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSubmit: (Report) -> Unit
) {
    var selectedResource by remember { mutableStateOf("LPG") }
    var selectedOutlet by remember { mutableStateOf<Outlet?>(null) }
    var selectedAvailability by remember { mutableStateOf<Availability?>(null) }
    var selectedQueue by remember { mutableStateOf<QueueLevel?>(null) }
    var note by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    // Image state: URI string or Bitmap
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // Camera launcher to take a photo
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            selectedImageUri = null
        }
    }

    // Gallery launcher to pick a photo
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            capturedBitmap = null
        }
    }

    val outlets = OutletRepository.getOutlets(selectedResource)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoftPageBg),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP HERO BANNER
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = ThemeBlue,
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(ThemeYellow, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Submit Report",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Help fellow citizens with live updates",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // FORM CONTAINER
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // USER NAME INPUT
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Your Name",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Sonam Tobgay", color = TextMuted) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ThemeBlue,
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }
                }

                // CATEGORY SELECTION
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "1. Category",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ChoiceCard(
                                text = "LPG Cylinder",
                                selected = selectedResource == "LPG",
                                modifier = Modifier.weight(1f)
                            ) {
                                selectedResource = "LPG"
                                selectedOutlet = null
                            }
                            ChoiceCard(
                                text = "Fuel Station",
                                selected = selectedResource == "Fuel",
                                modifier = Modifier.weight(1f)
                            ) {
                                selectedResource = "Fuel"
                                selectedOutlet = null
                            }
                        }
                    }
                }

                // OUTLET SELECTION
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "2. Select Outlet",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        SimpleDropdown(
                            selectedText = selectedOutlet?.name ?: "Select an outlet",
                            options = outlets.map { it.name },
                            onOptionSelected = { name ->
                                selectedOutlet = outlets.find { it.name == name }
                            }
                        )
                    }
                }

                // AVAILABILITY
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "3. Stock Availability",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ChoiceCard(
                                text = "In stock",
                                selected = selectedAvailability == Availability.AVAILABLE
                            ) { selectedAvailability = Availability.AVAILABLE }

                            ChoiceCard(
                                text = "Running low",
                                selected = selectedAvailability == Availability.LOW
                            ) { selectedAvailability = Availability.LOW }

                            ChoiceCard(
                                text = "Out of stock",
                                selected = selectedAvailability == Availability.OUT_OF_STOCK
                            ) { selectedAvailability = Availability.OUT_OF_STOCK }
                        }
                    }
                }

                // QUEUE LENGTH
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "4. Queue Length",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ChoiceCard(
                                text = "Short queue",
                                selected = selectedQueue == QueueLevel.SHORT
                            ) { selectedQueue = QueueLevel.SHORT }

                            ChoiceCard(
                                text = "Moderate queue",
                                selected = selectedQueue == QueueLevel.MODERATE
                            ) { selectedQueue = QueueLevel.MODERATE }

                            ChoiceCard(
                                text = "Long queue",
                                selected = selectedQueue == QueueLevel.LONG
                            ) { selectedQueue = QueueLevel.LONG }
                        }
                    }
                }

                // TAKE / ATTACH PHOTO SECTION
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "5. Outlet Photo (Optional)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Photo Preview if captured / selected
                        if (capturedBitmap != null || selectedImageUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, ThemeLightBlue, RoundedCornerShape(14.dp))
                            ) {
                                if (capturedBitmap != null) {
                                    Image(
                                        bitmap = capturedBitmap!!.asImageBitmap(),
                                        contentDescription = "Captured photo preview",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else if (selectedImageUri != null) {
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = "Gallery photo preview",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        capturedBitmap = null
                                        selectedImageUri = null
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(32.dp)
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Photo Buttons Row (Camera + Gallery)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Take Photo Button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(ThemeBlue)
                                    .clickable { cameraLauncher.launch(null) }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Take Photo",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Choose Photo Button
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(ThemeLightBlue)
                                    .clickable { galleryLauncher.launch("image/*") }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = ThemeBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "From Gallery",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ThemeBlue
                                    )
                                }
                            }
                        }
                    }
                }

                // ADDITIONAL NOTE
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "6. Additional Note (Optional)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Around 10 people in line.", color = TextMuted) },
                            minLines = 3,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ThemeBlue,
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }
                }

                // SUBMIT BUTTON
                Button(
                    onClick = {
                        if (selectedOutlet != null && selectedAvailability != null && selectedQueue != null) {
                            val photoUriString = selectedImageUri?.toString() ?: capturedBitmap?.let { "BITMAP_PHOTO" }

                            val newReport = Report(
                                id = "USER_${System.currentTimeMillis()}",
                                outletId = selectedOutlet!!.id,
                                availability = selectedAvailability!!,
                                queueLevel = selectedQueue!!,
                                reportedAt = System.currentTimeMillis(),
                                note = note,
                                imageUri = photoUriString,
                                userName = userName.ifBlank { "User Name" }
                            )
                            onSubmit(newReport)
                        }
                    },
                    enabled = selectedOutlet != null && selectedAvailability != null && selectedQueue != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ThemeBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Submit Community Report",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
