package com.example.indexcardapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ManageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_manage)

        val selectedProject = intent.getStringExtra("SELECTED_PROJECT")

        findViewById<Button>(R.id.btn_add_card).setOnClickListener {
            val intent2 = Intent(this, AddCardActivity::class.java)
            intent2.putExtra("SELECTED_PROJECT", selectedProject)
            startActivity(intent2)
        }

        findViewById<Button>(R.id.btn_learn).setOnClickListener {
            val intent = Intent(this, LearnCardsActivity::class.java)
            intent.putExtra("SELECTED_PROJECT", selectedProject)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_change_project).setOnClickListener {
            startActivity<MainActivity>()
        }
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}
