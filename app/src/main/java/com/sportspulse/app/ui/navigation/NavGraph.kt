package com.sportspulse.app.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.compose.runtime.getValue
import com.sportspulse.app.ui.components.BottomNavBar
import com.sportspulse.app.ui.screens.detail.ArticleDetailScreen
import com.sportspulse.app.ui.screens.feed.FeedScreen
import com.sportspulse.app.ui.screens.settings.SettingsScreen
import com.sportspulse.app.ui.screens.sports.SportsScreen

@Composable
fun SportsPulseNavGraph(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    // Explicit: bottom nav apare DOAR pe cele 3 tab-uri, niciodata pe ecranul de detaliu articol
    // (care nu e in bottomNavScreens si oricum incepe cu "article/").
    val showBottomBar = currentRoute != null &&
        bottomNavScreens.any { it.route == currentRoute } &&
        !currentRoute.startsWith("article/")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            // evita stive infinite cand apesi acelasi tab de mai multe ori
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(Screen.Feed.route) {
                FeedScreen(onArticleClick = { articleId ->
                    navController.navigate(Screen.ArticleDetail.createRoute(articleId))
                })
            }
            composable(Screen.Sports.route) {
                SportsScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
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
}
