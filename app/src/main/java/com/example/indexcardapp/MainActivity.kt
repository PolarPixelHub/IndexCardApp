package com.example.indexcardapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    private var projectListView: ListView? = null
    private var selectButton: Button? = null
    private var createButton: Button? = null
    private var deleteButton: Button? = null
    private var renameButton: Button? = null
    private var projectFiles: ArrayList<String>? = null
    private var adapter: ArrayAdapter<String>? = null
    private var selectedProject: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        projectListView = findViewById(R.id.project_list)
        selectButton = findViewById(R.id.btn_select_project)
        createButton = findViewById(R.id.btn_create_project)
        deleteButton = findViewById(R.id.btn_delete_project)
        renameButton = findViewById(R.id.btn_rename_project)

        // Load project list
        loadProjectList()

        // Handle item selection
        projectListView!!.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                selectedProject = projectFiles!![position]
                Toast.makeText(this@MainActivity, "Selected: $selectedProject", Toast.LENGTH_SHORT).show()
                view.isSelected = true
            }

        // Select Project Button
        selectButton!!.setOnClickListener { v: View? ->
            if (selectedProject != null) {
                Toast.makeText(
                    this@MainActivity,
                    "Loaded Project: $selectedProject", Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, ManageActivity::class.java)
                intent.putExtra("SELECTED_PROJECT", selectedProject)
                Log.d("Here: ", selectedProject!!)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "No project selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Create Project Button
        createButton!!.setOnClickListener { v: View? -> createProject() }

        // Delete Project Button
        deleteButton!!.setOnClickListener { v: View? -> deleteProject() }

        renameButton!!.setOnClickListener { renameProject() }

        findViewById<Button>(R.id.btn_option).setOnClickListener {
            startActivity<OptionActivity>()
        }
    }

    private fun loadProjectList() {
        // Load all ".json" files from the app's internal storage
        val directory: File = filesDir
        projectFiles = ArrayList()

        for (file in directory.listFiles()!!) {
            if (file.name.endsWith(".json")) {
                projectFiles!!.add(file.name)
            }
        }

        // Populate the ListView
        adapter = ArrayAdapter(this, R.layout.simple_list_item_1, R.id.customtext, projectFiles!!)
        projectListView?.adapter = adapter
    }

    private fun createProject() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create New Project")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Create") { _: DialogInterface?, _: Int ->
            val projectName = input.text.toString().trim { it <= ' ' }
            if (projectName.isNotEmpty()) {
                val fileName = "$projectName.json"
                val file = File(filesDir, fileName)
                try {
                    if (file.createNewFile()) {
                        Toast.makeText(
                            this,
                            "Project Created: $fileName",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadProjectList() // Refresh the list
                    } else {
                        Toast.makeText(
                            this,
                            "Project already exists!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Project name cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun deleteProject() {
        if (selectedProject != null) {
            AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Are you sure you want to delete '$selectedProject'?")
                .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                    val file: File? = selectedProject?.let { File(filesDir, it) }
                    if (file != null) {
                        if (file.delete()) {
                            Toast.makeText(
                                this,
                                "Deleted: $selectedProject",
                                Toast.LENGTH_SHORT
                            ).show()
                            selectedProject = null
                            loadProjectList() // Refresh list
                        } else {
                            Toast.makeText(
                                this,
                                "Failed to delete project",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        } else {
            Toast.makeText(this, "No project selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectProject() {
        val selectedIndex = projectListView?.checkedItemPosition
        if (selectedIndex != null && selectedIndex != -1) {
            selectedProject = projectFiles?.get(selectedIndex)
            Toast.makeText(this, "Loaded Project: $selectedProject", Toast.LENGTH_SHORT).show()
            // Transition to ManageActivity
            intent.putExtra("SELECTED_PROJECT", selectedProject)
            startActivity<ManageActivity>()
        } else {
            Toast.makeText(this, "No project selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renameProject() {
        if (selectedProject != null) {
            val currentFile = File(filesDir, selectedProject!!)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Rename Project")

            val input = EditText(this)
            input.hint = "Enter new name"
            builder.setView(input)

            builder.setPositiveButton("Rename") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isEmpty()) {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

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
            }

            builder.setNegativeButton("Cancel", null)
            builder.show()
        } else {
            Toast.makeText(this, "No project selected", Toast.LENGTH_SHORT).show()
        }
    }

    // Generic extension function to start an activity
    private inline fun <reified T : AppCompatActivity> startActivity() {
        startActivity(Intent(this, T::class.java))
    }
}