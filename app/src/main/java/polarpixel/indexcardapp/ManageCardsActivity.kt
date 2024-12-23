package polarpixel.indexcardapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ManageCardsActivity : AppCompatActivity() {

    private lateinit var flashcardManager: FlashcardManager
    private lateinit var cardListView: ListView
    private lateinit var cardAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_cards)

        // Load the selected project passed through Intent
        val selectedProject = intent.getStringExtra("SELECTED_PROJECT") ?: run {
            Toast.makeText(this, "Error: No project selected!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Initialize FlashcardManager
        flashcardManager = FlashcardManager(context = this, fileName = selectedProject)

        // Get list of flashcards
        val flashcards = flashcardManager.retrieveCards()

        // Setup ListView
        cardListView = findViewById(R.id.card_list_view)
        cardAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            flashcards.map { "${it.side1} - ${it.side2} (Correct: ${it.correctCount}, Difficulty: ${it.difficultyLevel})" }
        )
        cardListView.adapter = cardAdapter

        cardListView.setOnItemClickListener { _, _, position, _ ->
            showCardOptions(position)
        }

        findViewById<Button>(R.id.back_three_button).setOnClickListener {
            val intent = Intent(this, LearnCardsActivity::class.java)
            intent.putExtra("SELECTED_PROJECT", selectedProject)
            Log.d("Go to Learn: ", selectedProject)
            startActivity(intent)
        }
    }

    private fun showCardOptions(position: Int) {
        val selectedCard = flashcardManager.retrieveCards()[position]

        val dialog = AlertDialog.Builder(this)
            .setTitle("Manage Card")
            .setMessage("What would you like to do with this card?")
            .setPositiveButton("Reset Correct Count") { _, _ ->
                flashcardManager.resetCard(position)
                refreshCardList()
                Toast.makeText(this, "Card reset successfully.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Delete Card") { _, _ ->
                flashcardManager.deleteCard(position)
                refreshCardList()
                Toast.makeText(this, "Card deleted successfully.", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun refreshCardList() {
        val flashcards = flashcardManager.retrieveCards()
        cardAdapter.clear()
        cardAdapter.addAll(flashcards.map { "${it.side1} - ${it.side2} (Correct: ${it.correctCount}, Difficulty: ${it.difficultyLevel})" })
        cardAdapter.notifyDataSetChanged()
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}
