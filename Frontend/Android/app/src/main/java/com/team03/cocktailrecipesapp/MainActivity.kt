package com.team03.cocktailrecipesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.team03.cocktailrecipesapp.recipes.GetRecipesErrorListener
import com.team03.cocktailrecipesapp.recipes.GetRecipesListener
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import com.team03.cocktailrecipesapp.recipes.Recipe

//TODO: -> move to shared preferences
var userLoggedIn = false;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: -> get info from shared preferences
        if (!userLoggedIn) {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else {
            setContentView(R.layout.activity_main)

            // Get recipes from server and inflate list
            fillTrendingRecipesList()
        }
    }

    fun fillTrendingRecipesList() {
        val server = serverAPI(applicationContext)
        val listener = GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener = GetRecipesErrorListener(::onFailedGetRecipes)
        server.getRecipes(listener, errorListener)
    }

    fun onSuccessfulGetRecipes(recipe_list: List<Recipe>) {
        println("Get recipes() success...")
        // TODO: inflate list
        recipe_list.forEach { recipe -> println("$recipe") }
    }

    fun onFailedGetRecipes() {
        // TODO: display error message
        println("Get recipes() failed...")
    }

//    fun fillTrendingRecipesList(recipe_list: List<Recipe>) {
//        recipe_list.forEach() {
//            val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, trending_cocktail_list, false)
//            recipeCard.cocktail_name.text = it.name
//            recipeCard.cocktail_ratings.text = it.times_rated.toString()
//            recipeCard.cocktail_rating_bar.rating = it.rating
//            recipeCard.cocktail_difficulty.text =  it.difficulty.toString()
//            val preparation_time:String = it.preptime_minutes.toString() + " minutes"
//            recipeCard.cocktail_preparation_time.text = preparation_time
//            /*recipeCard.cocktail_image*/
//            trending_cocktail_list.addView(recipeCard)
//        }
//    }


    fun loginOnClick(view: View){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
    }


}