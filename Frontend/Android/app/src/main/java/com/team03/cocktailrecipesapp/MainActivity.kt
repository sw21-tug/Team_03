package com.team03.cocktailrecipesapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.team03.cocktailrecipesapp.error_listener.GetRecipesErrorListener
import com.team03.cocktailrecipesapp.listener.GetRecipesListener
import com.team03.cocktailrecipesapp.listener.Recipe
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_cocktail_detail.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*
import java.util.*

class MainActivity : SharedPreferencesActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var recipesLayout: LinearLayout

    var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        super.onStart()
        var language = shared.getString("Language", "")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
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

            progressBar = findViewById(R.id.progressbar);
            recipesLayout = findViewById(R.id.trending_cocktail_list)

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

        val b = intent.extras
        if (b != null) username = b.getString("username").toString()
    }

    fun getTrendingRecipesList() {
        val server = serverAPI(applicationContext)
        val listener =
            GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener =
            GetRecipesErrorListener(
                ::onFailedGetRecipes
            )
        server.getRecipes(listener, errorListener)
    }

    fun onSuccessfulGetRecipes(recipe_list: List<Recipe>) {
        fillTrendingRecipesList(recipe_list)
        progressBar.visibility = View.INVISIBLE;
        recipesLayout.visibility = View.VISIBLE
    }

    fun onFailedGetRecipes() {
        progressBar.visibility = View.INVISIBLE;
        recipesLayout.visibility = View.INVISIBLE
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

    fun fillTrendingRecipesList(recipe_list: List<Recipe>) {
        recipe_list.forEach() { recipe ->
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, recipesLayout, false)
            recipeCard.cocktail_name.text = recipe.name
            recipeCard.cocktail_ratings.text = recipe.times_rated.toString()
            recipeCard.cocktail_rating_bar.rating = recipe.rating
            recipeCard.cocktail_difficulty.text =  recipe.difficulty.toString()
            val preparationTime: String = recipe.preptime_minutes.toString() + " "+getString(R.string.minutes)
            recipeCard.cocktail_preparationtime.text = preparationTime
            recipeCard.cocktail_id.text = recipe.id.toString()
            addClickListener(recipeCard, recipesLayout.size)

            /* TODO: recipeCard.cocktail_image */
            recipesLayout.addView(recipeCard)
        }
    }

    fun profilePictureOnClick(view: View){
            //println("Username: " + username)
            //val intent = Intent(this, UserSettingsActivity::class.java)
            //startActivity(intent)

        val intent = Intent(this, UserProfileActivity::class.java)
        var bundle = Bundle()
        bundle.putString("username", userName)
        bundle.putInt("_creator_id", userId)
        intent.putExtras(bundle)
        startActivity((intent))
    }

    fun addClickListener(recipeCard: View, index: Int){

        recipeCard.imageView.setOnClickListener { openDetails(recipeCard) }
        recipeCard.linearLayout.setOnClickListener { openDetails(recipeCard) }
    }

    fun openDetails(recipeCard : View)
    {
        val intent = Intent(this, CocktailDetailActivity::class.java)
        var bundle = Bundle()

        bundle.putInt("cocktail_id", Integer.valueOf(recipeCard.cocktail_id.text.toString()))
        bundle.putString("cocktail_name", recipeCard.cocktail_name.text.toString())
        bundle.putString("cocktail_ratings", recipeCard.cocktail_ratings.text.toString())
        bundle.putFloat("cocktail_rating_bar", recipeCard.cocktail_rating_bar.rating)
        bundle.putString("cocktail_difficulty", recipeCard.cocktail_difficulty.text.toString())
        bundle.putString("preparation_time", recipeCard.cocktail_preparationtime.text.toString())

        intent.putExtras(bundle)
        startActivity(intent)
    }
}
