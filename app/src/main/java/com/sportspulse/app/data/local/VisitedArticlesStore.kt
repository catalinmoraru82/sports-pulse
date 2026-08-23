package com.sportspulse.app.data.local

import android.content.Context
import androidx.compose.runtime.mutableStateOf

/**
 * Tine minte ce articole a deschis userul, ca sa le putem afisa vizual diferit in feed
 * ("deja citit"). Persistat in SharedPreferences - supravietuieste la inchiderea aplicatiei.
 *
 * State-ul e tinut si in memorie (mutableStateOf) ca ArticleCard sa recompuna automat cand
 * se marcheaza un articol ca vizitat, fara sa citim SharedPreferences la fiecare recompunere.
 */
object VisitedArticlesStore {
    private const val PREFS_NAME = "sportspulse_prefs"
    private const val KEY_VISITED = "visited_article_ids"

    private val visitedIds = mutableStateOf<Set<String>>(emptySet())
    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        visitedIds.value = prefs.getStringSet(KEY_VISITED, emptySet()).orEmpty().toSet()
        initialized = true
    }

    // Citirea lui .value in interiorul unui @Composable il face automat observabil -
    // Compose recompune singur cand markVisited schimba setul.
    fun isVisited(articleId: String): Boolean = visitedIds.value.contains(articleId)

    fun markVisited(context: Context, articleId: String) {
        if (visitedIds.value.contains(articleId)) return
        val updated = visitedIds.value + articleId
        visitedIds.value = updated
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(KEY_VISITED, updated)
            .apply()
    }
}
