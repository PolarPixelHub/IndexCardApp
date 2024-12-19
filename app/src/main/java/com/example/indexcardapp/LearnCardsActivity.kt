package com.example.indexcardapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.properties.Delegates
import java.util.Random
import androidx.recyclerview.widget.GridLayoutManager

class LearnCardsActivity : AppCompatActivity() {

    // UI Components
    private lateinit var cardLabel: TextView
    private lateinit var drawingCanvas: PaintView
    private lateinit var flashcardManager: FlashcardManager
    private var currentIndex by Delegates.notNull<Int>() // Current index of the flashcard
    private lateinit var cards: MutableList<Flashcard> // List of flashcards
    private var toggleSides = false // Toggle between card sides
    private lateinit var savedCanvasView: ImageView // ImageView to display saved drawings
    private lateinit var drawList: MutableList<Bitmap> // List of drawing bitmaps
    private lateinit var drawAdapter: DrawAdapter // Adapter for displaying drawings in RecyclerView
    private lateinit var drawRecyclerView: RecyclerView // RecyclerView to display saved drawings

    private lateinit var togglePaintButton: Button
    private lateinit var paintButtonsContainer: LinearLayout
    private lateinit var buttonsContainer: LinearLayout
    private lateinit var saveButton: Button
    private lateinit var clearButton: Button
    private lateinit var backButtonTwo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_cards)

        // Initialize views
        initViews()

        // Load preferences and set visibility
        applyComponentVisibility()

        // Initialize the draw list and adapter
        drawList = mutableListOf() // Initialize the list of draw bitmaps
        drawAdapter = DrawAdapter(drawList) // Initialize the adapter with the draw list

        // Load the selected project from intent
        val selectedProject = intent.getStringExtra("SELECTED_PROJECT") ?: run {
            Toast.makeText(this, "Error: No project selected!", Toast.LENGTH_LONG).show()
            finish() // Exit the activity if no project is selected
            return
        }

        // Initialize flashcard manager and load cards
        flashcardManager = FlashcardManager(context = this, fileName = selectedProject)
        cards = flashcardManager.retrieveCards().toMutableList()

        // Set up UI listeners
        setupListeners()

        // Set up RecyclerView with the adapter
        drawRecyclerView.layoutManager = GridLayoutManager(this, 5)
        drawRecyclerView.setHasFixedSize(true)
        drawRecyclerView.adapter = drawAdapter

        // Show the first flashcard
        showNextCard()
    }

    // Initialize views and UI components
    private fun initViews() {
        cardLabel = findViewById(R.id.card_label)
        drawingCanvas = findViewById(R.id.drawing_Canvas)
        savedCanvasView = findViewById(R.id.saved_canvas_view)
        drawRecyclerView = findViewById(R.id.drawRecyclerView)
        togglePaintButton = findViewById(R.id.toggle_paint_button)
        paintButtonsContainer = findViewById(R.id.paint_buttons_container)
        buttonsContainer = findViewById(R.id.buttons_container)
        saveButton = findViewById(R.id.save_button)
        clearButton = findViewById(R.id.clear_button)
        backButtonTwo = findViewById(R.id.back_button_two)
    }

    // Set up listeners for buttons
    private fun setupListeners() {
        findViewById<Button>(R.id.flip_button).setOnClickListener { flipCard() }
        findViewById<Button>(R.id.correct_button).setOnClickListener { markCorrect() }
        findViewById<Button>(R.id.incorrect_button).setOnClickListener { markIncorrect() }
        findViewById<Button>(R.id.toggle_sides_button).setOnClickListener { toggleCardSide() }
        findViewById<Button>(R.id.show_next_button).setOnClickListener { showNextCard() }
        findViewById<Button>(R.id.back_button_two).setOnClickListener { hidePaintView() }
        findViewById<Button>(R.id.manage_cards_button).setOnClickListener { navigateToManageCards() }
        findViewById<Button>(R.id.back_button).setOnClickListener { navigateToManageActivity() }
        togglePaintButton.setOnClickListener {
            if (drawingCanvas.visibility == View.GONE) {
                showPaintView()
            } else {
                hidePaintView()
            }
        }

        saveButton.setOnClickListener { saveDrawing(drawingCanvas.getDrawingBitmap()) }
        clearButton.setOnClickListener { drawingCanvas.clear() }
        backButtonTwo.setOnClickListener { hidePaintView() }
    }

    // Display the next flashcard
    private fun showNextCard() {
        val filteredCards = cards.filter { it.correctCount < 10 } // Filter cards with less than 10 correct attempts

        if (filteredCards.isEmpty()) {
            // Show message if no cards are available
            showNoCardsAvailable()
            return
        }

        // Randomly select a flashcard that hasn't been fully mastered
        do {
            currentIndex = Random().nextInt(filteredCards.size)
        } while (filteredCards[currentIndex].correctCount >= 10)

        val card = filteredCards[currentIndex]
        cardLabel.text = if (toggleSides) card.side1 else card.side2 // Display the appropriate side
    }

    // Flip the current flashcard
    private fun flipCard() {
        if (currentIndex == -1 || cards.isEmpty()) {
            Toast.makeText(this, "No cards to flip.", Toast.LENGTH_SHORT).show()
            return
        }

        val card = cards[currentIndex]
        cardLabel.text = if (cardLabel.text == card.side1) card.side2 else card.side1 // Toggle between sides
    }

    // Mark the current flashcard as correct
    private fun markCorrect() {
        if (currentIndex == -1) {
            Toast.makeText(this, "No card selected.", Toast.LENGTH_SHORT).show()
            return
        }

        flashcardManager.markCorrect(currentIndex) // Mark the card as correct
        showNextCard() // Show the next card
    }

    // Mark the current flashcard as incorrect
    private fun markIncorrect() {
        if (currentIndex == -1) {
            Toast.makeText(this, "No card selected.", Toast.LENGTH_SHORT).show()
            return
        }

        flashcardManager.markIncorrect(currentIndex) // Mark the card as incorrect
        showNextCard() // Show the next card
    }

    // Toggle between card sides
    private fun toggleCardSide() {
        toggleSides = !toggleSides // Toggle the flag
        flipCard() // Flip the card to show the other side
    }

    // Save the current drawing
    private fun saveDrawing(bitmap: Bitmap) {
        drawList.add(bitmap) // Add the bitmap to the drawing list
        if (drawList.size > 10) {
            drawList.removeAt(0) // Keep only the last 10 items
        }
        drawAdapter.notifyDataSetChanged() // Notify the adapter to update the RecyclerView
        displaySavedCanvas(bitmap) // Display the saved drawing in the ImageView
        drawingCanvas.clear()
    }

    // Display the latest saved drawing in the ImageView
    private fun displaySavedCanvas(bitmap: Bitmap) {
        savedCanvasView.setImageBitmap(bitmap) // Set the bitmap on the ImageView
        savedCanvasView.visibility = View.VISIBLE // Make the ImageView visible
    }

    // Show/hide PaintView and related buttons
    private fun showPaintView() {
        drawingCanvas.visibility = View.VISIBLE
        paintButtonsContainer.visibility = View.VISIBLE
        buttonsContainer.visibility = View.GONE
    }

    private fun hidePaintView() {
        drawingCanvas.visibility = View.GONE
        paintButtonsContainer.visibility = View.GONE
        buttonsContainer.visibility = View.VISIBLE
    }

    // Navigate to the Manage Cards activity
    private fun navigateToManageCards() {
        val selectedProject = intent.getStringExtra("SELECTED_PROJECT") ?: return
        val intent = Intent(this, ManageCardsActivity::class.java).apply {
            putExtra("SELECTED_PROJECT", selectedProject)
        }
        startActivity(intent)
    }

    // Navigate to the Manage activity
    private fun navigateToManageActivity() {
        val selectedProject = intent.getStringExtra("SELECTED_PROJECT") ?: return
        val intent = Intent(this, ManageActivity::class.java).apply {
            putExtra("SELECTED_PROJECT", selectedProject)
        }
        startActivity(intent)
    }

    // Show message when no cards are available
    private fun showNoCardsAvailable() {
        currentIndex = -1
        cardLabel.text = getString(R.string.no_cards_available)
        Toast.makeText(this, R.string.no_cards_available, Toast.LENGTH_SHORT).show()
    }

    private fun applyComponentVisibility() {
        val sharedPreferences = getSharedPreferences("LearnCardsPreferences", MODE_PRIVATE)
        val showComponents = sharedPreferences.getBoolean("ShowComponents", true)

        // Set visibility based on preferences
        drawRecyclerView.visibility = if (showComponents) View.VISIBLE else View.GONE
        paintButtonsContainer.visibility = if (showComponents) View.GONE else View.GONE // Ensure hidden initially
    }

    override fun onResume() {
        super.onResume()
        applyComponentVisibility() // Reapply visibility when returning to this activity
    }
}




