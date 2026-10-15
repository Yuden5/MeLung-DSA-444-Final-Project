package com.example.drukgasapplicationfinalproject2026.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================
// DRUKGAS BRAND COLOR PALETTE
// 1. #094DB3 (Vibrant Royal Blue)
// 2. #FAB901 (Bright Gold Yellow)
// 3. #F5F6F8 (Soft Page Background)
// ============================================================

val ThemeBlue = Color(0xFF094DB3)         // Primary Blue (#094DB3)
val ThemeLightBlue = Color(0xFFDCE8F8)    // Light Blue Tint
val ThemeYellow = Color(0xFFFAB901)       // Bright Gold Yellow (#FAB901)

val SoftPageBg = Color(0xFFF5F6F8)        // Soft Page Background (#F5F6F8)
val CardWhite = Color(0xFFFFFFFF)         // White Card Surface
val BorderSlate = Color(0xFFE5E7EB)       // Border

val TextDark = Color(0xFF1F2937)          // Dark Text
val TextMuted = Color(0xFF6B7280)         // Muted Gray Text
val TextNavy = TextDark

// Status Colors for Progress Bars & Badges
val StatusGreen = Color(0xFF4CAF50)       // In-Stock Green
val StatusAmber = Color(0xFFFF9800)       // Running-Low Amber
val StatusRed = Color(0xFFE53935)         // Long-Queue / Out-of-Stock Red
val StatusGray = Color(0xFF9E9E9E)        // Unknown Gray

val StatusInStockBg = Color(0xFFD1FAE5)    // Mint Green Bg
val StatusInStockFg = Color(0xFF047857)    // Deep Green Text
val StatusInStockDot = StatusGreen

val StatusLowBg = Color(0xFFFEF3C7)       // Amber Bg
val StatusLowFg = Color(0xFFB45309)       // Deep Amber Text
val StatusLowDot = StatusAmber

val StatusOutOfStockBg = Color(0xFFFEE2E2)// Light Red Bg
val StatusOutOfStockFg = Color(0xFFB91C1C)// Deep Red Text
val StatusOutOfStockDot = StatusRed

val StatusUnknownBg = Color(0xFFF1F5F9)   // Slate Gray Bg
val StatusUnknownFg = Color(0xFF475569)   // Slate Text
val StatusUnknownDot = StatusGray

// Compatibility Aliases
val ThemeSkyBlue = ThemeBlue
val ThemePowderBlue = ThemeLightBlue
val ThemeSageYellow = ThemeYellow
val ThemeBlack = TextDark
val ThemeOffWhite = SoftPageBg
val ThemeRed = ThemeBlue
val BhutanOrange = ThemeBlue
val DeepNavy = ThemeBlue
val SoftSlate = SoftPageBg
