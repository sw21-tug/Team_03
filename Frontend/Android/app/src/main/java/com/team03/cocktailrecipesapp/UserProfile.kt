package com.team03.cocktailrecipesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val sw1 = findViewById<Switch>(R.id.language)

        sw1?.setOnCheckedChangeListener({ _ , isChecked ->
            changeLanguage()
        })
    }


    fun changeLanguage() : Boolean
    {
        // TODO change language
        return false
    }
}