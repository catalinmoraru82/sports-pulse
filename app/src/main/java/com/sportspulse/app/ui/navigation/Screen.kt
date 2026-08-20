package com.sportspulse.app.ui.navigation

sealed class Screen(val route: String) {
    data object Feed : Screen("feed")
    data object Sports : Screen("sports")
    data object Settings : Screen("settings")

    // ruta cu parametru: articleId
    data object ArticleDetail : Screen("article/{articleId}") {
        fun createRoute(articleId: String) = "article/$articleId"
    }
}

// Cele 3 tab-uri din bottom navigation (Feed / Sports / Settings), conform design-ului din Figma.
val bottomNavScreens = listOf(Screen.Feed, Screen.Sports, Screen.Settings)
