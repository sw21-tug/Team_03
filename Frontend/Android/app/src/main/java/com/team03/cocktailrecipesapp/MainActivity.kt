package com.team03.cocktailrecipesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.trending_cocktail_list.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recipe_list = getRecipes()
        fillTrendingRecipesList(recipe_list)
    }

    fun fillTrendingRecipesList(recipe_list: List<Recipe>) {
        recipe_list.forEach() {
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, trending_cocktail_list, false)
            recipeCard.cocktail_name.text = it.name
            recipeCard.cocktail_ratings.text = it.times_rated.toString()
            recipeCard.cocktail_rating_bar.rating = it.rating
            recipeCard.cocktail_difficulty.text =  it.difficulty.toString()
            recipeCard.cocktail_preparation_time.text = it.preptime_minutes.toString()
            /*recipeCard.cocktail_image*/
            trending_cocktail_list.addView(recipeCard)
        }
    }

    fun getRecipes() : List<Recipe> {
        val recipe = Recipe("cocktail XYZ", 4.0F, 2)
        val recipe_list = listOf(recipe, recipe, recipe, recipe, recipe, recipe)
        return recipe_list
    }
}