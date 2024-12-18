package com.example.indexcardapp


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddCardActivity : AppCompatActivity() {

    private lateinit var inputSide1: EditText
    private lateinit var inputSide2: EditText
    private lateinit var addCardButton: Button
    private lateinit var backButton: Button
    private lateinit var flashcardManager: FlashcardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        // Initialize views
        inputSide1 = findViewById(R.id.input_side1)
        inputSide2 = findViewById(R.id.input_side2)
        addCardButton = findViewById(R.id.add_card_button)
        backButton = findViewById(R.id.back_button)

        // Load the selected project passed through Intent
        val selectedProject = intent.getStringExtra("SELECTED_PROJECT")
        if (selectedProject != null) {
            Log.d("AddCards", selectedProject)
        }

        if (selectedProject == null) {
            Toast.makeText(this, "Error: No project selected!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Initialize FlashcardManager
        flashcardManager = FlashcardManager(context = this, fileName = selectedProject)

        // Handle Add Card Button
        addCardButton.setOnClickListener {
            val side1 = inputSide1.text.toString().trim()
            val side2 = inputSide2.text.toString().trim()

            if (side1.isEmpty() || side2.isEmpty()) {
                Toast.makeText(this, "Please fill both fields.", Toast.LENGTH_SHORT).show()
            } else {
                flashcardManager.addCard(side1, side2, selectedProject)
                Toast.makeText(this, "Card added successfully!", Toast.LENGTH_SHORT).show()
                inputSide1.text.clear()
                inputSide2.text.clear()
            }
        }

        // Handle Back Button
        backButton.setOnClickListener {
            finish()
        }
    }
}
