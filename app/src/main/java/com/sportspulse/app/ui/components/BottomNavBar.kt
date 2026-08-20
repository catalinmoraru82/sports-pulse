package com.sportspulse.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.sportspulse.app.ui.navigation.Screen
import com.sportspulse.app.ui.navigation.bottomNavScreens

private fun screenIcon(screen: Screen) = when (screen) {
    Screen.Feed -> Icons.Filled.Newspaper
    Screen.Sports -> Icons.Filled.BookmarkBorder
    Screen.Settings -> Icons.Filled.Settings
    else -> Icons.Filled.Newspaper
}

private fun screenLabel(screen: Screen) = when (screen) {
    Screen.Feed -> "Feed"
    Screen.Sports -> "Sports"
    Screen.Settings -> "Settings"
    else -> ""
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomNavScreens.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(screen) },
                icon = { Icon(screenIcon(screen), contentDescription = screenLabel(screen)) },
                label = { Text(screenLabel(screen)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}
