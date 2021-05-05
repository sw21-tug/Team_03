package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.trending_cocktail_list.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*

//TODO: -> move to shared preferences
var userId = 0;
var userName = "";

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userId == 0) {
            val shared: SharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            userId = shared.getInt("userId", 0);
        }

        if (userId == 0) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else {
            val shared_editor: SharedPreferences.Editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
            shared_editor.putInt("userId", userId).apply()

            setContentView(R.layout.activity_main)

            val recipe_list = getRecipes()
            val recommended_recipes = getRecommendedRecipes(recipe_list)

            fillTrendingRecipesList(recipe_list)
            fillRecommendedRecipesList(recommended_recipes)
        }
    }

    fun fillTrendingRecipesList(recipe_list: List<Recipe>) {
        recipe_list.forEach() {
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, trending_cocktail_list, false)
            recipeCard.cocktail_name.text = it.name
            recipeCard.cocktail_ratings.text = it.times_rated.toString()
            recipeCard.cocktail_rating_bar.rating = it.rating
            recipeCard.cocktail_difficulty.text =  it.difficulty.toString()
            val preparation_time:String = it.preptime_minutes.toString() + " " + resources.getString(R.string.minutes)
            recipeCard.cocktail_preparation_time.text = preparation_time
            /*recipeCard.cocktail_image*/
            trending_cocktail_list.addView(recipeCard)
        }
    }

    fun fillRecommendedRecipesList(recommended_recipes: List<Recipe>) : Boolean {
        // TODO: inflate recommeneded recipes
        return false
    }

    fun getRecipes() : List<Recipe> {
        val recipe = Recipe("cocktail XYZ", 4.0F, 2)
        val recipe_list = listOf(recipe, recipe, recipe, recipe, recipe, recipe)
        return recipe_list
    }

    fun getRecommendedRecipes(recipe_list: List<Recipe>) : List<Recipe> {
        // TODO: get recommended recipes (top 5 rated)
        return recipe_list
    }

    fun loginOnClick(view: View){
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
    }
}