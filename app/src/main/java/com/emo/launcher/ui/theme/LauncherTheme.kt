package com.emo.launcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkMonochrome = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,

    secondary = Color(0xFFBDBDBD),
    onSecondary = Color.Black,

    background = Color(0xFF080808),
    onBackground = Color.White,

    surface = Color(0xFF111111),
    onSurface = Color.White,

    surfaceVariant = Color(0xFF1C1C1C),
    onSurfaceVariant = Color(0xFFCCCCCC),

    outline = Color(0xFF555555)
)

private val LightMonochrome = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,

    secondary = Color(0xFF555555),
    onSecondary = Color.White,

    background = Color(0xFFF7F7F7),
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,

    surfaceVariant = Color(0xFFEAEAEA),
    onSurfaceVariant = Color(0xFF444444),

    outline = Color(0xFF888888)
)

@Composable
fun LauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme =
            if (darkTheme) DarkMonochrome
            else LightMonochrome,
        content = content
    )
}