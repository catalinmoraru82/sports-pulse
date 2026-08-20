package com.sportspulse.app

import android.app.Application

class SportsPulseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Loc pentru initializari globale viitoare (analytics, crash reporting etc.)
    }
}
