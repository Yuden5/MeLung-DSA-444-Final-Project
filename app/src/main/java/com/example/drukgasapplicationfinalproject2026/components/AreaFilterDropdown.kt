package com.example.drukgasapplicationfinalproject2026.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AreaFilterDropdown(
    selectedArea: String,
    areas: List<String>,
    onAreaSelected: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Box {

        Card(
            onClick = {
                expanded = true
            },

            shape = RoundedCornerShape(12.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {

            Row(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = selectedArea,

                    style =
                        MaterialTheme.typography.bodyMedium,

                    fontWeight =
                        FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.size(4.dp)
                )

                Icon(
                    imageVector =
                        Icons.Default.KeyboardArrowDown,

                    contentDescription =
                        "Select area",

                    modifier =
                        Modifier.size(20.dp)
                )
            }
        }


        DropdownMenu(
            expanded = expanded,

            onDismissRequest = {
                expanded = false
            }
        ) {

            areas.forEach { area ->

                DropdownMenuItem(
                    text = {
                        Text(area)
                    },

                    onClick = {

                        onAreaSelected(area)

                        expanded = false
                    }
                )
            }
        }
    }
}