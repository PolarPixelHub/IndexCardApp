package com.example.indexcardapp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

data class Flashcard(
    var side1: String,
    var side2: String,
    var correctCount: Int = 0,
    var incorrectCount: Int = 0,
    var difficultyLevel: String = "normal",
    var lastCorrectTime: String? = null,
    var timePeriods: Int = 1
)

class FlashcardManager(private val context: Context, private val fileName: String) {
    val cards: MutableList<Flashcard> = loadCards()

    private fun loadCards(): MutableList<Flashcard> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return mutableListOf()

        return try {
            val jsonArray = JSONArray(file.readText())
            List(jsonArray.length()) { index ->
                val jsonObject = jsonArray.getJSONObject(index)
                Flashcard(
                    jsonObject.getString("side1"),
                    jsonObject.getString("side2"),
                    jsonObject.getInt("correct_count"),
                    jsonObject.getInt("incorrect_count"),
                    jsonObject.getString("difficulty_level"),
                    jsonObject.optString("last_correct_time", null.toString()),
                    jsonObject.getInt("time_periods")
                )
            }.toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    private fun saveCards() {
        val file = File(context.filesDir, fileName)
        try {
            val writer = FileWriter(file)
            val jsonArray = JSONArray()
            cards.forEach { card ->
                val jsonObject = JSONObject().apply {
                    put("side1", card.side1)
                    put("side2", card.side2)
                    put("correct_count", card.correctCount)
                    put("incorrect_count", card.incorrectCount)
                    put("difficulty_level", card.difficultyLevel)
                    put("last_correct_time", card.lastCorrectTime)
                    put("time_periods", card.timePeriods)
                }
                jsonArray.put(jsonObject)
            }
            writer.write(jsonArray.toString(4))  // Pretty print with indentation
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun addCard(side1: String, side2: String, selectedProject: String) {
        cards.add(Flashcard(side1, side2))
        saveCards()
    }

    fun markCorrect(index: Int) {
        if (index in cards.indices) {
            val card = cards[index]
            card.correctCount++
            card.incorrectCount = 0
            card.lastCorrectTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
            updateDifficulty(index)
            saveCards()
        }
    }

    fun markIncorrect(index: Int) {
        if (index in cards.indices) {
            val card = cards[index]
            card.incorrectCount++
            card.correctCount = 0
            updateDifficulty(index)
            saveCards()
        }
    }

    fun resetCard(index: Int) {
        if (index in cards.indices) {
            val card = cards[index]
            card.correctCount = 0
            card.incorrectCount = 0
            updateDifficulty(index)
            saveCards()
        }
    }

    fun deleteCard(index: Int) {
        if (index in cards.indices) {
            cards.removeAt(index)
            saveCards()
        }
    }

    private fun updateDifficulty(index: Int) {
        val card = cards[index]
        when {
            card.incorrectCount >= 10 -> card.difficultyLevel = "relearn"
            card.incorrectCount >= 5 -> card.difficultyLevel = "difficult"
            card.incorrectCount >= 3 -> card.difficultyLevel = "mistakes"
            else -> card.difficultyLevel = "normal"
        }
    }

    fun retrieveCards(): List<Flashcard> = cards
}
