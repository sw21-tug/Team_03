package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

abstract class SharedPreferencesActivity : AppCompatActivity() {
    lateinit var shared: SharedPreferences
    lateinit var shared_editor: SharedPreferences.Editor
    lateinit var difficultyArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        shared_editor= getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
    }

    override fun onStart() {
        super.onStart()
        difficultyArray = arrayOf(
            resources.getString(R.string.difficulty_very_easy),
            resources.getString(R.string.difficulty_easy),
            resources.getString(R.string.difficulty_medium),
            resources.getString(R.string.difficulty_hard),
            resources.getString(R.string.difficulty_very_hard)
        )
    }

    fun getRecipeDifficutly(difficulty: Int) : String
    {
        return difficultyArray[difficulty-1]
    }
}