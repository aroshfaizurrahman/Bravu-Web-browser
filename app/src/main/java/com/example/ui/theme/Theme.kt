package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ElegantPurpleAccent,
    onPrimary = ElegantPurpleContainer,
    primaryContainer = ElegantPurpleContainer,
    onPrimaryContainer = ElegantPurpleAccent,
    secondary = BravuShieldCyan,
    onSecondary = Color.Black,
    background = ElegantDarkBg,
    onBackground = ElegantTextMain,
    surface = ElegantDarkSurface,
    onSurface = ElegantTextMain,
    surfaceVariant = ElegantDarkVariant,
    onSurfaceVariant = ElegantTextMuted,
    tertiary = PrankYellow,
    error = PrankPink
)

private val LightColorScheme = darkColorScheme(
    primary = ElegantPurpleAccent,
    onPrimary = ElegantPurpleContainer,
    primaryContainer = ElegantPurpleContainer,
    onPrimaryContainer = ElegantPurpleAccent,
    secondary = BravuShieldCyan,
    onSecondary = Color.Black,
    background = ElegantDarkBg,
    onBackground = ElegantTextMain,
    surface = ElegantDarkSurface,
    onSurface = ElegantTextMain,
    surfaceVariant = ElegantDarkVariant,
    onSurfaceVariant = ElegantTextMuted,
    tertiary = PrankYellow,
    error = PrankPink
)

@Composable
fun BravuTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
