package com.example.indexcardapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ManageActivity : AppCompatActivity() {

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_manage)

        // Show the loading screen when starting the activity
        showLoadingScreen()

        // Load data or perform operations
        performDataLoading()

        val selectedProject = intent.getStringExtra("SELECTED_PROJECT")

        // Button for adding a card
        findViewById<Button>(R.id.btn_add_card).setOnClickListener {
            navigateToActivity<AddCardActivity>(selectedProject)
        }

        // Button for learning cards
        findViewById<Button>(R.id.btn_learn).setOnClickListener {
            navigateToActivity<LearnCardsActivity>(selectedProject)
        }

        // Button for switching back to the main activity
        findViewById<Button>(R.id.btn_change_project).setOnClickListener {
            navigateToMainActivity()
        }
    }

    // Navigate to any activity with selected project
    private inline fun <reified T : AppCompatActivity> navigateToActivity(selectedProject: String?) {
        val intent = Intent(this, T::class.java).apply {
            putExtra("SELECTED_PROJECT", selectedProject)
        }
        startActivity(intent)
    }

    // Navigate back to MainActivity
    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    // Show loading screen
    private fun showLoadingScreen() {
        val loadingScreen = LayoutInflater.from(this).inflate(R.layout.loading_screen, null)
        loadingDialog = AlertDialog.Builder(this)
            .setView(loadingScreen)
            .setCancelable(false) // Prevent interaction
            .create()

        loadingDialog.show()
    }

    // Hide loading screen
    private fun hideLoadingScreen() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    private fun performDataLoading() {
        // Simulating data loading with a delay
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoadingScreen() // Hide loading screen after data is loaded
        }, 3000) // Simulate loading delay (3 seconds)
    }
}
