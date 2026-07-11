package com.debdev.bantuin.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = OrangePrimary,
    primaryVariant = OrangeDark,
    secondary = OrangePrimary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.WhiteCompat,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    error = ErrorRed
)

private val DarkColorPalette = darkColors(
    primary = OrangePrimary,
    primaryVariant = OrangeLight,
    secondary = OrangePrimary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.WhiteCompat,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    error = ErrorRed
)

@Composable
fun BantuinTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (isDarkMode) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}

// Helper kecil supaya "Color.WhiteCompat" bisa dipakai tanpa import tambahan berulang
private object Color {
    val WhiteCompat = androidx.compose.ui.graphics.Color.White
}
