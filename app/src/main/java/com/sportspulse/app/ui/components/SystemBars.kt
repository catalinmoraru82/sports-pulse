package com.sportspulse.app.ui.components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Seteaza explicit culoarea iconitelor barei de sistem (ceas, baterie, semnal).
 *
 * Fiecare ecran cu propriul fundal de top-bar trebuie sa apeleze asta cu valoarea
 * corecta - altfel, o data ce un ecran seteaza iconite deschise (ex: FeedScreen,
 * care are banda neagra fixa), ele RAMAN asa si pe ecranele urmatoare daca acelea
 * nu reafirma explicit propria lor valoare (Theme.kt seteaza asta o singura data
 * la nivel de root, nu se re-executa automat doar pentru ca ai navigat in alt ecran).
 *
 * @param useLightIcons true = iconite deschise/albe (pt fundaluri inchise la culoare),
 *                       false = iconite inchise/negre (pt fundaluri deschise la culoare)
 */
@Composable
fun ForceStatusBarIcons(useLightIcons: Boolean) {
    val activity = LocalContext.current as? Activity
    val view = LocalView.current
    if (activity != null && !view.isInEditMode) {
        SideEffect {
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !useLightIcons
        }
    }
}
