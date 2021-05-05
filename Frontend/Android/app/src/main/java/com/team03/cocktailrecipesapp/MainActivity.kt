package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.team03.cocktailrecipesapp.recipes.GetRecipesErrorListener
import com.team03.cocktailrecipesapp.recipes.GetRecipesListener

import com.team03.cocktailrecipesapp.ui.login.LoginActivity

import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.team03.cocktailrecipesapp.recipes.Recipe
import kotlinx.android.synthetic.main.progress_indicator.*
import kotlinx.android.synthetic.main.error_msg_indicator.*

import kotlinx.android.synthetic.main.trending_cocktail_list.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*

var userId = 0;
var userName = "";

class MainActivity : SharedPreferencesActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        // try to get a previously saved userId
        if (userId == 0) {
            userId = shared.getInt("userId", 0);
        }
        if (userId == 0) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else {
            shared_editor.putInt("userId", userId).apply()
            setContentView(R.layout.activity_main)

            // change profile picture according to selected language
            val language = shared.getString("Language", "");
            var avatarImgae = findViewById<ImageButton>(R.id.imgBtnAvatar);
            if (language.equals("kv")) {
                avatarImgae.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
            } else {
                avatarImgae.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar ));
            }
            getTrendingRecipesList()
        }
    }

    fun getTrendingRecipesList() {
        val server = serverAPI(applicationContext)
        val listener = GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener = GetRecipesErrorListener(::onFailedGetRecipes)
        server.getRecipes(listener, errorListener)
    }

    fun onSuccessfulGetRecipes(recipe_list: List<Recipe>) {
        progressBar.visibility = View.GONE
        fillTrendingRecipesList(recipe_list)
    }

    fun onFailedGetRecipes() {
        txtViewErrorMsg.text = resources.getString(R.string.failed_to_load_recipes_from_server)
        txtViewErrorMsgContainer.visibility = View.VISIBLE
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

    fun fillTrendingRecipesList(recipe_list: List<Recipe>) {
        recipe_list.forEach() { recipe ->
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, trending_cocktail_list, false)
            recipeCard.cocktail_name.text = recipe.name
            recipeCard.cocktail_ratings.text = recipe.times_rated.toString()
            recipeCard.cocktail_rating_bar.rating = recipe.rating
            recipeCard.cocktail_difficulty.text =  recipe.difficulty.toString()
            val preparationTime: String = recipe.preptime_minutes.toString() + " minutes"
            recipeCard.cocktail_preparation_time.text = preparationTime
            /* TODO: recipeCard.cocktail_image */
            trending_cocktail_list.addView(recipeCard)
        }
    }

    fun profilePictureOnClick(view: View){
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
    }
}