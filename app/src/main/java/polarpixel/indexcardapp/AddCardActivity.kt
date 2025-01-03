package polarpixel.indexcardapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class AddCardActivity : AppCompatActivity() {

    private lateinit var inputSide1: EditText
    private lateinit var inputSide2: EditText
    private lateinit var addCardButton: Button
    private lateinit var backButton: Button
    private lateinit var flashcardManager: FlashcardManager
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        // Initialize views
        initializeViews()

        // Load the selected project passed through Intent
        val selectedProject = intent.getStringExtra(EXTRA_SELECTED_PROJECT)
        if (selectedProject.isNullOrEmpty()) {
            showErrorAndFinish("Error: No project selected!")
        } else {
            Log.d(TAG, "Selected Project: $selectedProject")
            initializeFlashcardManager(selectedProject)
        }

        // Handle Add Card Button
        if (selectedProject != null) {
            handleAddCardButton(selectedProject)
        }

        // Handle Back Button
        handleBackButton()

        loadAd()
    }

    private fun initializeViews() {
        inputSide1 = findViewById(R.id.input_side1)
        inputSide2 = findViewById(R.id.input_side2)
        addCardButton = findViewById(R.id.add_card_button)
        backButton = findViewById(R.id.back_button)
        mAdView = findViewById(R.id.adView)
    }

    private fun showErrorAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun initializeFlashcardManager(selectedProject: String) {
        flashcardManager = FlashcardManager(context = this, fileName = selectedProject)
    }

    private fun handleAddCardButton(selectedProject: String) {
        addCardButton.setOnClickListener {
            val side1 = inputSide1.text.toString().trim()
            val side2 = inputSide2.text.toString().trim()

            if (side1.isEmpty() || side2.isEmpty()) {
                showToast("Please fill both fields.")
            } else {
                flashcardManager.addCard(side1, side2, selectedProject)
                showToast("Card added successfully!")
                clearInputFields()
            }
        }
    }

    private fun handleBackButton() {
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun clearInputFields() {
        inputSide1.text.clear()
        inputSide2.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "AddCardActivity"
        const val EXTRA_SELECTED_PROJECT = "SELECTED_PROJECT"
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, getAdMobExtras())
            .build()

        mAdView.loadAd(adRequest)
    }
    private fun getAdMobExtras(): Bundle {
        return Bundle().apply {
            putString("max_ad_content_rating", "G")
            putString("npa", "1")
        }
    }
}
