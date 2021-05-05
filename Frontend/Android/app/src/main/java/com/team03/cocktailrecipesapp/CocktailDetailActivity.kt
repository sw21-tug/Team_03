package com.team03.cocktailrecipesapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CocktailDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)


        val b = intent.extras
        var recipe_id = -1 // or other values

        if (b != null) recipe_id = b.getInt("cocktail_id")

        var textV  = findViewById<TextView>(R.id.textView)
        textV.text = recipe_id.toString()

    }
}