package com.example.drukgasapplicationfinalproject2026.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drukgasapplicationfinalproject2026.R
import com.example.drukgasapplicationfinalproject2026.ui.theme.ThemeBlue

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background Image Asset (welcome.jpg)
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "Welcome Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Glassmorphic Translucent Card at the Bottom (Matching attached image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                .background(Color.White.copy(alpha = 0.65f))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.85f),
                    RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
                )
                .padding(start = 28.dp, end = 28.dp, top = 36.dp, bottom = 36.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Title
                Text(
                    text = "Welcome to\nDrukGas!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B),
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description Subtitle
                Text(
                    text = "Stay updated on LPG and fuel availability across 12 districts of Bhutan with community-powered reports, all in one place!",
                    fontSize = 14.sp,
                    color = Color(0xFF475569),
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Full Width Black "Get Started" Pill Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Black)
                        .clickable { onGetStarted() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Footer Row: Are you new here? Sign Up
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Are you new here? ",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "Sign Up",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemeBlue,
                        modifier = Modifier.clickable { onGetStarted() }
                    )
                }
            }
        }
    }
}
