package com.abhilash.apps.animecatalogue.view.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val ThemeGreenPrimary = Color(0xFF4C6707)
val ThemeGreenSecondary = Color(0xFF5A6147)
val ThemeGreenTertiary = Color(0xFF396660)
val ThemeGreenBackground = Color(0xFFFEFCF4)

val ThemeColorWhite = Color(0xFFFFFFFF)

enum class ThemeColor(
    val lightColor: ColorScheme,
    val darkColor: ColorScheme
) {
    THEME_DARK_GREEN(
        lightColor = lightColorScheme(
            primary = ThemeGreenPrimary,
            onPrimary = ThemeColorWhite,
            primaryContainer = Color(0xFFCDEf84),
            onPrimaryContainer = Color(0xFF141f00),

            secondary = ThemeGreenSecondary,
            onSecondary = ThemeColorWhite,
            secondaryContainer = Color(0xFFdee6c5),
            onSecondaryContainer = Color(0xFF171e09),

            tertiary = ThemeGreenTertiary,
            onTertiary = ThemeColorWhite,
            tertiaryContainer = Color(0xFFBCECE4),
            onTertiaryContainer = Color(0xFF00201D),

            background = ThemeGreenBackground,
            onBackground = Color(0xFF1B1C17),

            surface = Color(0xFFFEFCF4),
            onSurface = Color(0xFF1B1C17),
            surfaceVariant = Color(0xFFE2E4d4),
            onSurfaceVariant = Color(0xFF45483C),

            error = Color(0xFFBA1A1A),
            onError = ThemeColorWhite,
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),

            outline = Color(0xFF76786B),
        ),

        darkColor = darkColorScheme(
            primary = Color(0xFFB2D26B),
            onPrimary = Color(0xFF253500),
            primaryContainer = Color(0xFF384E00),
            onPrimaryContainer = Color(0xFFCDEf84),

            secondary = Color(0xFFC2CAAA),
            onSecondary = Color(0xFF2C331D),
            secondaryContainer = Color(0xFF424A31),
            onSecondaryContainer = Color(0xFFDEE6C5),

            tertiary = Color(0xFFA0D0C8),
            onTertiary = Color(0xFF013732),
            tertiaryContainer = Color(0xFF204E48),
            onTertiaryContainer = Color(0xFFBCECE4),

            background = Color(0xFF1B1C17),
            onBackground = Color(0xFFE4E3DB),

            surface = Color(0xFF1B1C17),
            onSurface = Color(0xFFE4E3DB),
            surfaceVariant = Color(0xFF45483C),
            onSurfaceVariant = Color(0xFFC6C8b9),

            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),

            outline = Color(0xFF909284),
        )
    ),


    THEME_LIGHT_GREEN(
        lightColor = lightColorScheme(
            primary = md_theme_light_primary,
            onPrimary = md_theme_light_onPrimary,
            primaryContainer = md_theme_light_primaryContainer,
            onPrimaryContainer = md_theme_light_onPrimaryContainer,
            secondary = md_theme_light_secondary,
            onSecondary = md_theme_light_onSecondary,
            secondaryContainer = md_theme_light_secondaryContainer,
            onSecondaryContainer = md_theme_light_onSecondaryContainer,
            tertiary = md_theme_light_tertiary,
            onTertiary = md_theme_light_onTertiary,
            tertiaryContainer = md_theme_light_tertiaryContainer,
            onTertiaryContainer = md_theme_light_onTertiaryContainer,
            error = md_theme_light_error,
            errorContainer = md_theme_light_errorContainer,
            onError = md_theme_light_onError,
            onErrorContainer = md_theme_light_onErrorContainer,
            background = md_theme_light_background,
            onBackground = md_theme_light_onBackground,
            surface = md_theme_light_surface,
            onSurface = md_theme_light_onSurface,
            surfaceVariant = md_theme_light_surfaceVariant,
            onSurfaceVariant = md_theme_light_onSurfaceVariant,
            outline = md_theme_light_outline,
            inverseOnSurface = md_theme_light_inverseOnSurface,
            inverseSurface = md_theme_light_inverseSurface,
            inversePrimary = md_theme_light_inversePrimary,
            surfaceTint = md_theme_light_surfaceTint,
            outlineVariant = md_theme_light_outlineVariant,
            scrim = md_theme_light_scrim,
        ),

        darkColor = darkColorScheme(
            primary = md_theme_dark_primary,
            onPrimary = md_theme_dark_onPrimary,
            primaryContainer = md_theme_dark_primaryContainer,
            onPrimaryContainer = md_theme_dark_onPrimaryContainer,
            secondary = md_theme_dark_secondary,
            onSecondary = md_theme_dark_onSecondary,
            secondaryContainer = md_theme_dark_secondaryContainer,
            onSecondaryContainer = md_theme_dark_onSecondaryContainer,
            tertiary = md_theme_dark_tertiary,
            onTertiary = md_theme_dark_onTertiary,
            tertiaryContainer = md_theme_dark_tertiaryContainer,
            onTertiaryContainer = md_theme_dark_onTertiaryContainer,
            error = md_theme_dark_error,
            errorContainer = md_theme_dark_errorContainer,
            onError = md_theme_dark_onError,
            onErrorContainer = md_theme_dark_onErrorContainer,
            background = md_theme_dark_background,
            onBackground = md_theme_dark_onBackground,
            surface = md_theme_dark_surface,
            onSurface = md_theme_dark_onSurface,
            surfaceVariant = md_theme_dark_surfaceVariant,
            onSurfaceVariant = md_theme_dark_onSurfaceVariant,
            outline = md_theme_dark_outline,
            inverseOnSurface = md_theme_dark_inverseOnSurface,
            inverseSurface = md_theme_dark_inverseSurface,
            inversePrimary = md_theme_dark_inversePrimary,
            surfaceTint = md_theme_dark_surfaceTint,
            outlineVariant = md_theme_dark_outlineVariant,
            scrim = md_theme_dark_scrim,
        )
    )
}


