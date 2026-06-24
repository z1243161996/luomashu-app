package com.mispec.luomashu.ui.theme

import androidx.compose.material3.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Material Design 3 Light Theme ──
// Desktop-tuned: higher contrast, wider gamut for diverse monitor calibration.
// Warm paper palette with crisp typography hierarchy.

object AppColors {
    // Surface — paper warmth, never pure white, elevated contrast for desktops
    val bgPrimary   = Color(0xFFFAF8F5)
    val bgSecondary = Color(0xFFF3F0EC)
    val bgTertiary  = Color(0xFFEBE7E2)
    val surfaceDim  = Color(0xFFE4DFDA)

    // Outlines — slightly heavier for desktop readability
    val outlineLight = Color(0xFFCCC8C2)
    val outlineDark  = Color(0xFF8A8680)

    // Typography — MD3 on-surface with raised contrast
    val textPrimary   = Color(0xFF18181A)
    val textSecondary = Color(0xFF57534E)
    val textTertiary  = Color(0xFF6B6560)
    val textInverse   = Color(0xFFF5F3F0)

    // Brand accent — Indigo Blue
    val accent       = Color(0xFF4F5ED8)
    val accentMuted  = Color(0xFFEDEFFC)
    val accentText   = Color(0xFF3640A8)

    // Semantic
    val success      = Color(0xFF19804A)
    val successMuted = Color(0xFFE6F4EA)
    val danger       = Color(0xFFC62828)
    val dangerMuted  = Color(0xFFFFEBEE)

    // Category palette — saturated for light bg, desktop-safe
    val catNeuro      = Color(0xFFD94145)
    val catCognitive  = Color(0xFF4F5ED8)
    val catPersonality = Color(0xFF8E24AA)
    val catKnowledge  = Color(0xFF0288D1)
    val catSensory    = Color(0xFFE65100)
    val catPlayground = Color(0xFF00897B)
}

private val LightScheme = lightColorScheme(
    primary            = AppColors.accent,
    onPrimary          = Color.White,
    primaryContainer   = AppColors.accentMuted,
    onPrimaryContainer = AppColors.accentText,
    secondary          = Color(0xFF57534E),
    onSecondary        = Color.White,
    background         = AppColors.bgPrimary,
    onBackground       = AppColors.textPrimary,
    surface            = AppColors.bgSecondary,
    onSurface          = AppColors.textPrimary,
    surfaceVariant     = AppColors.bgTertiary,
    onSurfaceVariant   = AppColors.textSecondary,
    outline            = AppColors.outlineLight,
    outlineVariant     = AppColors.outlineDark,
    error              = AppColors.danger,
    errorContainer     = AppColors.dangerMuted,
    onError            = Color.White,
    inverseSurface     = Color(0xFF18181A),
    inverseOnSurface   = AppColors.textInverse,
    inversePrimary     = Color(0xFFB8BDF0),
    surfaceTint        = AppColors.accent,
)



private val DarkScheme = darkColorScheme(
    primary            = Color(0xFFB8BDF0),
    onPrimary          = Color(0xFF1A1A5E),
    primaryContainer   = Color(0xFF3A3F8E),
    onPrimaryContainer = Color(0xFFDEE0FF),
    secondary          = Color(0xFFC9C5C0),
    onSecondary        = Color(0xFF1A1A1A),
    background         = Color(0xFF111113),
    onBackground       = Color(0xFFE6E3E0),
    surface            = Color(0xFF1A1A1E),
    onSurface          = Color(0xFFE6E3E0),
    surfaceVariant     = Color(0xFF2A2A2E),
    onSurfaceVariant   = Color(0xFFC9C5C0),
    outline            = Color(0xFF5A5550),
    outlineVariant     = Color(0xFF3A3530),
    error              = Color(0xFFFFB4AB),
    errorContainer     = Color(0xFF93000A),
    onError            = Color(0xFF690005),
    inverseSurface     = Color(0xFFE6E3E0),
    inverseOnSurface   = Color(0xFF1A1A1E),
    inversePrimary     = Color(0xFF4F5ED8),
    surfaceTint        = Color(0xFFB8BDF0),
)


@Composable
fun LuomashuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkScheme else LightScheme,
        content = content
    )
}
