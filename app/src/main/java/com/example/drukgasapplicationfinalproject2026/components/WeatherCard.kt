
package com.example.drukgasapplicationfinalproject2026.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.drukgasapplicationfinalproject2026.data.WeatherResponse

@Composable
fun WeatherCard(
    weather: WeatherResponse?,
    isLoading: Boolean,
    error: String?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "WEATHER",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF69766B),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            when {
                isLoading -> {
                    Text(
                        text = "…",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF26352C)
                    )

                    Text(
                        text = "Loading",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF606B62)
                    )
                }

                error != null -> {
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF26352C)
                    )

                    Text(
                        text = "Unavailable",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF606B62)
                    )
                }

                weather != null -> {
                    Text(
                        text = "${weather.current.temperature_2m}°",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF26352C),
                        maxLines = 1
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = weatherDescription(
                            weather.current.weather_code
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF606B62),
                        maxLines = 2
                    )
                }

                else -> {
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF26352C)
                    )

                    Text(
                        text = "No data",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF606B62)
                    )
                }
            }
        }
    }
}