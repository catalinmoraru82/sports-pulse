package com.sportspulse.app.ui.theme

import androidx.compose.runtime.mutableStateOf

/**
 * Suprascrierea manuala a temei, setata din switch-ul "Dark mode" din Settings.
 * null   = urmeaza tema sistemului (comportamentul implicit, dinainte)
 * true   = dark fortat
 * false  = light fortat
 *
 * E un singleton simplu in memorie - se reseteaza la restart. Cand vrem sa retinem
 * alegerea intre sesiuni, aici e locul unde adaugam persistenta (DataStore Preferences).
 */
object ThemeState {
    var darkModeOverride = mutableStateOf<Boolean?>(null)
}
