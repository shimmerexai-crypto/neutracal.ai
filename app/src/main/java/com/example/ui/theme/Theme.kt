package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NutracalColorScheme = lightColorScheme(
    primary = NutracalBlack,
    onPrimary = NutracalWhite,
    primaryContainer = NutracalCardBg,
    onPrimaryContainer = NutracalBlack,
    secondary = NutracalSecondary,
    onSecondary = NutracalWhite,
    tertiary = NutracalGreen,
    onTertiary = NutracalWhite,
    background = NutracalWhite,
    onBackground = NutracalBlack,
    surface = NutracalWhite,
    onSurface = NutracalBlack,
    surfaceVariant = NutracalCardBg,
    onSurfaceVariant = NutracalBlack,
    outline = NutracalCardBorder,
    outlineVariant = NutracalSubtle
)

@Composable
fun NutracalTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NutracalColorScheme,
        typography = Typography,
        content = content
    )
}
