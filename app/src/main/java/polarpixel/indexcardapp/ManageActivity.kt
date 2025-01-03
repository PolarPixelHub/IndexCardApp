package polarpixel.indexcardapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class ManageActivity : AppCompatActivity() {

    private lateinit var loadingDialog: AlertDialog
    private lateinit var mAdView: AdView

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

        mAdView = findViewById(R.id.adView)

        loadAd()
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
