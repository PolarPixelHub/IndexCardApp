package polarpixel.indexcardapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.app.AppCompatDelegate

class OptionActivity : AppCompatActivity() {
    private lateinit var nightModeSwitch: SwitchCompat

    private val PREF_NAME = "LearnCardsPreferences"
    private val NIGHT_MODE_KEY = "NightMode"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        nightModeSwitch = findViewById(R.id.switch_toggle_night)

        // Load saved preferences
        val sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val isNightModeEnabled = sharedPreferences.getBoolean(NIGHT_MODE_KEY, false)

        // Set the switches to their saved states
        nightModeSwitch.isChecked = isNightModeEnabled

        // Night mode switch listener
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            with(sharedPreferences.edit()) {
                putBoolean(NIGHT_MODE_KEY, isChecked)
                apply()
            }
            Toast.makeText(this, "Theme updated", Toast.LENGTH_SHORT).show()
        }

        // Back button functionality
        val backButton = findViewById<Button>(R.id.back_option_button)
        backButton.setOnClickListener {
            finish() // Closes the activity and returns to the previous screen
        }
    }
}


