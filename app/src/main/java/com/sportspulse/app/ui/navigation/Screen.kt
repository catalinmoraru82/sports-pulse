package com.sportspulse.app.ui.navigation

sealed class Screen(val route: String) {
    data object Feed : Screen("feed")
    data object Settings : Screen("settings")

    // ruta cu parametru: articleId
    data object ArticleDetail : Screen("article/{articleId}") {
        fun createRoute(articleId: String) = "article/$articleId"
    }
}
