package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MysticGold,
    secondary = MossGreen,
    tertiary = AstralRose,
    background = NightCosmos,
    surface = CosmicSurface,
    onPrimary = PureBlack,
    onSecondary = OffWhite,
    onTertiary = OffWhite,
    onBackground = OffWhite,
    onSurface = OffWhite,
    surfaceVariant = CosmicSurfaceLight,
    onSurfaceVariant = OffWhite
)

private val LightColorScheme = lightColorScheme(
    primary = MossGreen,
    secondary = MysticGold,
    tertiary = AstralBlue,
    background = OffWhite,
    surface = CosmicSurfaceLight,
    onPrimary = OffWhite,
    onSecondary = PureBlack,
    onTertiary = OffWhite,
    onBackground = NightCosmos,
    onSurface = NightCosmos,
    surfaceVariant = CosmicSurface,
    onSurfaceVariant = OffWhite
)

@Composable
fun NucleoEsenciaTheme(
    darkTheme: Boolean = true, // Force to cosmic dark theme for the mystical feel
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve our crafted esoteric palette
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
