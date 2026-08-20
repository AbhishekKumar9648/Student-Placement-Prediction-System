package com.example.placement.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = SurfaceLight,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = PurpleDark,
    secondary = PurpleSecondary,
    onSecondary = SurfaceLight,
    secondaryContainer = PurpleLight,
    onSecondaryContainer = PurpleDark,
    tertiary = OceanBlue,
    onTertiary = SurfaceLight,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = SurfaceLight,
    onSurface = TextDark,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextMuted,
    outline = BorderLight,
    error = ErrorRed,
    onError = SurfaceLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PurpleSecondary,
    onPrimary = SurfaceLight,
    primaryContainer = PurpleDark,
    onPrimaryContainer = PurpleLight,
    secondary = SkyBlue,
    onSecondary = TextDark,
    background = Color(0xFF15161E),
    onBackground = Color(0xFFE2E4EB),
    surface = Color(0xFF1E202B),
    onSurface = Color(0xFFE2E4EB),
    surfaceVariant = Color(0xFF282A38),
    onSurfaceVariant = Color(0xFFA5A9B8),
    outline = Color(0xFF3C3F50),
    error = Color(0xFFEF5350),
    onError = SurfaceLight
)

@Composable
fun PlacementPredictorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
