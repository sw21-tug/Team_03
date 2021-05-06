package com.team03.cocktailrecipesapp

import com.team03.cocktailrecipesapp.recipes.*
import kotlinx.android.synthetic.main.progress_indicator.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CocktailDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)

        val b = intent.extras
        var recipe_id = -1 // or other values

        if (b != null) recipe_id = b.getInt("cocktail_id")

        getRecipe(recipe_id)
    }

    fun getRecipe(recipe_id: Int) {
        val server = serverAPI(applicationContext)
        //val listener = GetRecipeListener(::onSuccessfulGetRecipe)
        //val errorListener = GetRecipeErrorListener(::onFailedGetRecipe)
        //server.getRecipe(recipe_id, userId,listener, errorListener)
    }

   /* fun onSuccessfulGetRecipe(recipe: RecipeDetail) {
        //progressBar.visibility = View.GONE
        println("Got recipe from server!")
        //println(recipe.toString())

    }*/

    fun onFailedGetRecipe() {
        println("Failed to get recipe from server!")
        //txtViewErrorMsg.text = resources.getString(R.string.failed_to_load_recipes_from_server)
        //txtViewErrorMsgContainer.visibility = View.VISIBLE
        //Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

}