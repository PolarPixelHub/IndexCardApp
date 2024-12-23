package polarpixel.indexcardapp

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class ThemeMode(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "theme_prefs"
        private const val IS_DARK_MODE = "is_dark_mode"
    }

    // Save the theme preference
    private fun saveThemePreference(isDarkMode: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(IS_DARK_MODE, isDarkMode)
            apply()
        }
    }

    // Check if Dark Mode is enabled
    fun isDarkModeEnabled(): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_DARK_MODE, false)
    }

    // Apply the selected theme and save preference
    fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        saveThemePreference(isDarkMode)
    }
}
