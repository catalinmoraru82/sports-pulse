package com.sportspulse.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sportspulse.app.ui.screens.detail.ArticleDetailScreen
import com.sportspulse.app.ui.screens.feed.FeedScreen
import com.sportspulse.app.ui.screens.settings.SettingsScreen

// Nu mai avem bottom navigation bar - Feed e ecranul principal, Settings se acceseaza
// prin iconita din top-right, iar pagina de articol are doar buton "inapoi".
@Composable
fun SportsPulseNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Feed.route,
    ) {
        composable(Screen.Feed.route) {
            FeedScreen(
                onArticleClick = { articleId ->
                    navController.navigate(Screen.ArticleDetail.createRoute(articleId))
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
            route = Screen.ArticleDetail.route,
            arguments = listOf(navArgument("articleId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId") ?: return@composable
            ArticleDetailScreen(
                articleId = articleId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
