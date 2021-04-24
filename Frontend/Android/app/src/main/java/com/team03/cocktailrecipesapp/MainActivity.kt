package com.team03.cocktailrecipesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.cocktail_list_view.*
import kotlinx.android.synthetic.main.cocktail_list_view_card.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addRecipesToList(getRecipes())
    }

    fun addRecipesToList(recipe_list: List<Recipe>) : Boolean {
        recipe_list.forEach() {
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.cocktail_list_view_card, cocktail_list, false)
            recipeCard.cocktail_name.text = it.name
            recipeCard.cocktail_ratings.text = "0"
            recipeCard.cocktail_rating_bar.rating = 3F
            recipeCard.cocktail_difficulty.text =  it.difficulty.toString()
            recipeCard.cocktail_preparation_time.text = it.preptime_minutes.toString()
            /*recipeCard.cocktail_image*/
            cocktail_list.addView(recipeCard)
        }
        return true
    }

    fun getRecipes() : List<Recipe> {
        val recipe_1 = Recipe("cocktail 1")
        val recipe_2 = Recipe("cocktail 2")
        val recipe_3 = Recipe("cocktail 3")
        val recipe_4 = Recipe("cocktail 4")
        val recipe_list = listOf(recipe_1, recipe_2, recipe_3,recipe_4)
        return recipe_list
    }
}