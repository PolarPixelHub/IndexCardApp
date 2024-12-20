package com.example.indexcardapp

// File: App.kt
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Anwenden des Nachtmodus basierend auf gespeicherten Einstellungen
        applyTheme()
    }

    fun applyTheme() {
        val sharedPreferences = getSharedPreferences("LearnCardsPreferences", Context.MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("NightMode", false)
        val nightMode = if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
