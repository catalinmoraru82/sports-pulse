package com.sportspulse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sportspulse.app.ui.navigation.SportsPulseNavGraph
import com.sportspulse.app.ui.theme.SportsPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SportsPulseTheme {
                SportsPulseNavGraph()
            }
        }
    }
}
