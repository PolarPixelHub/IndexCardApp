package com.example.indexcardapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class OptionActivity : AppCompatActivity() {
    private lateinit var toggleVisibilitySwitch: SwitchCompat
    private val PREF_NAME = "LearnCardsPreferences"
    private val SHOW_COMPONENTS_KEY = "ShowComponents"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        toggleVisibilitySwitch = findViewById(R.id.switch_toggle_visibility)

        // Load saved preference
        val sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val showComponents = sharedPreferences.getBoolean(SHOW_COMPONENTS_KEY, true)
        toggleVisibilitySwitch.isChecked = showComponents

        // Update preference when switch is toggled
        toggleVisibilitySwitch.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences.edit()) {
                putBoolean(SHOW_COMPONENTS_KEY, isChecked)
                apply()
            }
            Toast.makeText(this, "Settings updated", Toast.LENGTH_SHORT).show()
        }

        val backButton = findViewById<Button>(R.id.back_option_button)
            backButton.setOnClickListener {
            finish() // Closes the activity and returns to the previous screen
        }
    }
}

