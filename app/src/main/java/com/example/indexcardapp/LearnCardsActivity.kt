package com.example.indexcardapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.properties.Delegates

class LearnCardsActivity : AppCompatActivity() {
    private lateinit var cardLabel: TextView
    private lateinit var drawingCanvas: PaintView // Updated to PaintView
    private lateinit var flashcardManager: FlashcardManager
    private var currentIndex by Delegates.notNull<Int>()
    private lateinit var cards: MutableList<Flashcard>
    private var toggleSides = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_cards)

        // Initialize views
        cardLabel = findViewById(R.id.card_label)
        drawingCanvas = findViewById(R.id.drawing_Canvas) // Initialize PaintView
        val flipButton: Button = findViewById(R.id.flip_button)
        val correctButton: Button = findViewById(R.id.correct_button)
        val incorrectButton: Button = findViewById(R.id.incorrect_button)
        val toggleSidesButton: Button = findViewById(R.id.toggle_sides_button)
        val showNextButton: Button = findViewById(R.id.show_next_button)

        // Log to confirm drawingCanvas is initialized
        Log.d("LearnCardsActivity", "drawingCanvas initialized: $drawingCanvas")

        // Load the selected project passed through Intent
        val selectedProject = intent.getStringExtra("SELECTED_PROJECT") ?: run {
            Toast.makeText(this, "Error: No project selected!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Initialize FlashcardManager
        flashcardManager = FlashcardManager(context = this, fileName = selectedProject)
        cards = flashcardManager.retrieveCards().toMutableList()

        // Set up button listeners
        flipButton.setOnClickListener { flipCard() }
        correctButton.setOnClickListener { markCorrect() }
        incorrectButton.setOnClickListener { markIncorrect() }
        toggleSidesButton.setOnClickListener { toggleCardSide() }
        showNextButton.setOnClickListener { showNextCard() }

        currentIndex = -1
        showNextCard()

        findViewById<Button>(R.id.manage_cards_button).setOnClickListener {
            val intent = Intent(this, ManageCardsActivity::class.java)
            intent.putExtra("SELECTED_PROJECT", selectedProject)
            Log.d("Go to Manage: ", selectedProject)
            startActivity(intent)
        }

        // Handle Back Button
        findViewById<Button>(R.id.back_two_button).setOnClickListener {
            val intent2 = Intent(this, ManageActivity::class.java)
            intent2.putExtra("SELECTED_PROJECT", selectedProject)
            Log.d("Go to Manage: ", selectedProject)
            startActivity(intent2)
        }

        findViewById<Button>(R.id.clear_button).setOnClickListener {
            drawingCanvas.clear()
        }
    }

    // Function to display the next card, keeping track of drawing
    private fun showNextCard() {
        val filteredCards = cards.filter { it.correctCount < 10 }

        if (filteredCards.isEmpty()) {
            Toast.makeText(this, R.string.no_cards_available, Toast.LENGTH_SHORT).show()
            currentIndex = -1
            cardLabel.text = getString(R.string.no_cards_available)
            return
        }

        currentIndex = Random().nextInt(filteredCards.size)
        val card = filteredCards[currentIndex]

        while (card.correctCount >= 10) {
            currentIndex = Random().nextInt(filteredCards.size)
            val newCard = filteredCards[currentIndex]
            if (newCard.correctCount < 10) {
                break
            }
        }

        cardLabel.text = if (toggleSides) card.side1 else card.side2
    }

    // Function to flip card
    private fun flipCard() {
        if (currentIndex == -1 || cards.isEmpty()) {
            Toast.makeText(this, "No cards to flip.", Toast.LENGTH_SHORT).show()
            return
        }

        val card = cards[currentIndex]
        cardLabel.text = if (cardLabel.text == card.side1) card.side2 else card.side1
    }

    // Function to mark the card as correct
    private fun markCorrect() {
        if (currentIndex == -1) {
            Toast.makeText(this, "No card selected.", Toast.LENGTH_SHORT).show()
            return
        }

        flashcardManager.markCorrect(currentIndex)
        showNextCard()
    }

    // Function to mark the card as incorrect
    private fun markIncorrect() {
        if (currentIndex == -1) {
            Toast.makeText(this, "No card selected.", Toast.LENGTH_SHORT).show()
            return
        }

        flashcardManager.markIncorrect(currentIndex)
        showNextCard()
    }

    // Toggle the card sides (front/back)
    private fun toggleCardSide() {
        toggleSides = !toggleSides
        flipCard()
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}

