package com.example.drukgasapplicationfinalproject2026.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ThemeBlue,
    secondary = ThemeLightBlue,
    tertiary = ThemeYellow,
    background = TextDark,
    surface = TextDark,
    onPrimary = Color.White,
    onSecondary = TextDark,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ThemeBlue,
    secondary = ThemeLightBlue,
    tertiary = ThemeYellow,
    background = SoftPageBg,
    surface = CardWhite,
    onPrimary = Color.White,
    onSecondary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun DrukGasApplicationFinalProject2026Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
