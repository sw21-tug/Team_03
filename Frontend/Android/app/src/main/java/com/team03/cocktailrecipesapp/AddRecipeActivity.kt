package com.team03.cocktailrecipesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_recipe.*


class AddRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        timer_picker_minutes.minValue = 1
        timer_picker_minutes.maxValue = 59
        difficulty_picker.minValue = 1
        difficulty_picker.maxValue = 5
    }

    fun onClickManageIngredients(view: View) {
        val intent = Intent(this, AddIngredientsActivity::class.java)
        startActivity(intent)
    }
}
