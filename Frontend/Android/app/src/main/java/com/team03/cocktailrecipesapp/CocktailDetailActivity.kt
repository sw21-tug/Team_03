package com.team03.cocktailrecipesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.team03.cocktailrecipesapp.recipes.*
import kotlinx.android.synthetic.main.progress_indicator.*

class CocktailDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)
        getRecipe()
    }

    fun getRecipe() {
        val server = serverAPI(applicationContext)
        val listener = GetRecipeListener(::onSuccessfulGetRecipe)
        val errorListener = GetRecipeErrorListener(::onFailedGetRecipe)
        server.getRecipe(4,3,listener, errorListener)
    }

    fun onSuccessfulGetRecipe(recipe: RecipeDetail) {
        //progressBar.visibility = View.GONE
        println("Got recipe from server!")
        //println(recipe.toString())

    }

    fun onFailedGetRecipe() {
        println("Failed to get recipe from server!")
        //txtViewErrorMsg.text = resources.getString(R.string.failed_to_load_recipes_from_server)
        //txtViewErrorMsgContainer.visibility = View.VISIBLE
        //Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

}