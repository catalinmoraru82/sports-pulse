package com.sportspulse.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sportspulse.app.ui.screens.feed.FeedScreen
import com.sportspulse.app.ui.screens.settings.SettingsScreen
import com.sportspulse.app.ui.screens.webview.ArticleWebViewScreen

// Nu mai avem bottom navigation bar - Feed e ecranul principal, Settings se acceseaza
// prin iconita din top-right. La tap pe un articol, deschidem pagina originala a
// sursei intr-un WebView in aplicatie - nu mai exista un ecran custom de detaliu.
@Composable
fun SportsPulseNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Feed.route,
    ) {
        composable(Screen.Feed.route) {
            FeedScreen(
                onArticleClick = { article ->
                    val url = article.originalUrl
                    if (!url.isNullOrBlank()) {
                        navController.navigate(Screen.ArticleWebView.createRoute(article.id, url))
                    }
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.ArticleWebView.route,
            arguments = listOf(
                navArgument("articleId") { type = NavType.StringType },
                navArgument("encodedUrl") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId") ?: return@composable
            val encodedUrl = backStackEntry.arguments?.getString("encodedUrl") ?: return@composable
            ArticleWebViewScreen(
                articleId = articleId,
                url = Screen.ArticleWebView.decodeUrl(encodedUrl),
                onBack = { navController.popBackStack() },
            )
        }
    }
}
