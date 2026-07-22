package com.brkckr.fakeiglive.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = InstagramPink,
    onPrimary = Color.White,
    secondary = InstagramPurple,
    tertiary = InstagramOrange,
    background = DarkCanvas,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    onSurfaceVariant = MutedInk,
)

private val LightColorScheme = lightColorScheme(
    primary = InstagramPink,
    onPrimary = Color.White,
    secondary = InstagramPurple,
    tertiary = InstagramOrange,
    background = Canvas,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    onSurfaceVariant = MutedInk,
)

// extension to access dimensions easily via materialtheme
val MaterialTheme.spacing: Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current

@Composable
fun FakeIGLiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalDimensions provides Dimensions()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content,
        )
    }
}
