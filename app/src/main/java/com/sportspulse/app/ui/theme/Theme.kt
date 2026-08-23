package com.sportspulse.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimaryLight,
    onPrimary = OnPrimaryLight,
    surface = SurfaceLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outlineVariant = OutlineVariantLight,
    error = ErrorLight,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimaryDark,
    onPrimary = OnPrimaryDark,
    surface = SurfaceDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outlineVariant = OutlineVariantDark,
    error = ErrorDark,
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
)

@Composable
fun SportsPulseTheme(
    // Daca userul a ales explicit din Settings, respectam alegerea lui; altfel urmam sistemul.
    darkTheme: Boolean = ThemeState.darkModeOverride.value ?: isSystemInDarkTheme(),
    // Material You (culori dinamice din wallpaper) - disponibil din Android 12 (API 31),
    // exact minSdk-ul nostru. Dezactivat implicit ca sa pastram brandul portocaliu
    // consistent; poate fi activat din Settings daca vrei sa oferi optiunea userului.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Fundalul barei de status vine deja din edge-to-edge (enableEdgeToEdge() in MainActivity) +
    // Surface-urile proprii cu statusBarsPadding() - nu mai setam statusBarColor manual (API deprecat).
    // Controlam doar culoarea iconitelor sistemului (ceas, baterie etc.) sa fie lizibile pe fundal.
    val activity = LocalContext.current as? Activity
    val view = LocalView.current
    if (activity != null && !view.isInEditMode) {
        SideEffect {
            val controller = WindowCompat.getInsetsController(activity.window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}
