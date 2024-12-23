package polarpixel.indexcardapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var projectListView: ListView
    private lateinit var selectButton: Button
    private lateinit var createButton: Button
    private lateinit var deleteButton: Button
    private lateinit var renameButton: Button
    private lateinit var projectFiles: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private var selectedProject: String? = null
    private lateinit var buttonsContainerMain: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("LearnCardsPreferences", MODE_PRIVATE)
        val isNightModeEnabled = sharedPreferences.getBoolean("NightMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        setContentView(R.layout.activity_main)

        // Initialize views
        initializeViews()

        // Load project list
        loadProjectList()

        // Handle item selection
        setupItemClickListener()

        // Set up button listeners
        setupButtonListeners()
    }

    private fun initializeViews() {
        projectListView = findViewById(R.id.project_list)
        selectButton = findViewById(R.id.btn_select_project)
        createButton = findViewById(R.id.btn_create_project)
        deleteButton = findViewById(R.id.btn_delete_project)
        renameButton = findViewById(R.id.btn_rename_project)
        buttonsContainerMain = findViewById(R.id.buttons_container_main)
    }

    private fun loadProjectList() {
        // Load all ".json" files from the app's internal storage
        val directory: File = filesDir
        projectFiles = ArrayList()

        directory.listFiles()?.forEach { file ->
            if (file.name.endsWith(".json")) {
                projectFiles.add(file.name)
            }
        }

        // Populate the ListView
        adapter = ArrayAdapter(this, R.layout.simple_list_item_1, R.id.customtext, projectFiles)
        projectListView.adapter = adapter
    }

    private fun setupItemClickListener() {
        projectListView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            selectedProject = projectFiles[position]
            Toast.makeText(this, "Selected: $selectedProject", Toast.LENGTH_SHORT).show()
            view.isSelected = true
        }
    }

    private fun setupButtonListeners() {
        selectButton.setOnClickListener { onSelectProjectClick() }
        createButton.setOnClickListener { onCreateProjectClick() }
        deleteButton.setOnClickListener { onDeleteProjectClick() }
        renameButton.setOnClickListener { onRenameProjectClick() }
        findViewById<Button>(R.id.btn_option).setOnClickListener { startActivity<OptionActivity>() }
    }

    private fun onSelectProjectClick() {
        selectedProject?.let {
            Toast.makeText(this, "Loaded Project: $it", Toast.LENGTH_SHORT).show()
            Intent(this, ManageActivity::class.java).apply {
                putExtra("SELECTED_PROJECT", it)
                startActivity(this)
            }
        } ?: run {
            Toast.makeText(this, "No project selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onCreateProjectClick() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create New Project")

        val input = EditText(this)
        input.hint = "Enter new project name"
        builder.setView(input)

        builder.setPositiveButton("Create") { _, _ ->
            val projectName = input.text.toString().trim()
            if (projectName.isNotEmpty()) {
                val fileName = "$projectName.json"
                val file = File(filesDir, fileName)
                if (file.createNewFile()) {
                    Toast.makeText(this, "Project Created: $fileName", Toast.LENGTH_SHORT).show()
                    loadProjectList() // Refresh the list
                } else {
                    Toast.makeText(this, "Project already exists!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Project name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun onDeleteProjectClick() {
        selectedProject?.let {
            AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Are you sure you want to delete '$it'?")
                .setPositiveButton("Yes") { _, _ ->
                    val file = File(filesDir, it)
                    if (file.delete()) {
                        Toast.makeText(this, "Deleted: $it", Toast.LENGTH_SHORT).show()
                        selectedProject = null
                        loadProjectList() // Refresh list
                    } else {
                        Toast.makeText(this, "Failed to delete project", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        } ?: run {
            Toast.makeText(this, "No project selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onRenameProjectClick() {
        selectedProject?.let { currentFileName ->
            val currentFile = File(filesDir, currentFileName)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Rename Project")

            val input = EditText(this).apply { hint = "Enter new name" }
            builder.setView(input)

            builder.setPositiveButton("Rename") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val newFileName = "$newName.json"
                    val newFile = File(filesDir, newFileName)
                    if (newFile.exists()) {
                        Toast.makeText(this, "A project with this name already exists", Toast.LENGTH_SHORT).show()
                    } else if (currentFile.renameTo(newFile)) {
                        Toast.makeText(this, "Renamed to: $newFileName", Toast.LENGTH_SHORT).show()
                        selectedProject = newFileName // Update selectedProject
                        loadProjectList() // Refresh project list
                    } else {
                        Toast.makeText(this, "Failed to rename project", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        } ?: run {
            Toast.makeText(this, "No project selected", Toast.LENGTH_SHORT).show()
        }
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }

}
