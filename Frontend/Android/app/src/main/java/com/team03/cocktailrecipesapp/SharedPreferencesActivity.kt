package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

abstract class SharedPreferencesActivity : AppCompatActivity() {
    lateinit var shared: SharedPreferences
    lateinit var shared_editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        shared_editor= getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
    }
}