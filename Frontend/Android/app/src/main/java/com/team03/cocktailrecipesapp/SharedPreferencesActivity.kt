package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class SharedPreferencesActivity : AppCompatActivity() {
    lateinit var shared: SharedPreferences
    lateinit var shared_editor: SharedPreferences.Editor
    lateinit var difficultyArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        difficultyArray = arrayOf(
            resources.getString(R.string.difficulty_very_easy),
            resources.getString(R.string.difficulty_easy),
            resources.getString(R.string.difficulty_medium),
            resources.getString(R.string.difficulty_hard),
            resources.getString(R.string.difficulty_very_hard)
        )
        shared = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        shared_editor= getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
    }


    fun getRecipeDifficutly(difficulty: Int) : String
    {
        return difficultyArray[difficulty-1]
    }
}