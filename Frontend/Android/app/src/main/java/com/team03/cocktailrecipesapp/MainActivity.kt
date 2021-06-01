package com.team03.cocktailrecipesapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.team03.cocktailrecipesapp.error_listener.GetRecipesErrorListener
import com.team03.cocktailrecipesapp.listener.GetRecipesListener
import com.team03.cocktailrecipesapp.listener.Recipe
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.recommended_cocktail_list_card.view.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_id
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_name
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_rating_bar
import java.util.*

class MainActivity : SharedPreferencesActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var recipesLayout: LinearLayout
<<<<<<< HEAD
    lateinit var recommendedCocktailList: LinearLayout
=======
    lateinit var txtSeeAll : TextView
>>>>>>> 9381e4f62b70e4a98bb3b2b9ee853478b67988c8

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
            txtSeeAll = findViewById(R.id.txtViewSeeAll)
            progressBar = findViewById(R.id.progressbar);
            recipesLayout = findViewById(R.id.trending_cocktail_list)
            recommendedCocktailList = findViewById(R.id.recommended_cocktail_list)

            txtSeeAll.setOnClickListener() {
                    val intent = Intent(this, SeeAllRecipies::class.java)
                    startActivity(intent)
            }

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




    fun onClickAddRecipe(view: View) {
        val intent = Intent(this, AddRecipeActivity::class.java)
        startActivity(intent)
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
        Collections.shuffle(recipe_list)
        val recommendedRecipeList = recipe_list.subList(0, if (recipe_list.size >= 5) 5 else recipe_list.size)
        var trendingRecipeList = recipe_list.subList(recommendedRecipeList.size, recipe_list.size).sortedByDescending { it.rating }
        trendingRecipeList = trendingRecipeList.subList(0, if (trendingRecipeList.size >= 15) 15 else trendingRecipeList.size)
        fillTrendingRecipesList(trendingRecipeList)
        fillRecommendetRecipesList(recommendedRecipeList)
        progressBar.visibility = View.INVISIBLE;
        recipesLayout.visibility = View.VISIBLE
    }

    fun onFailedGetRecipes() {
        progressBar.visibility = View.INVISIBLE;
        recipesLayout.visibility = View.INVISIBLE
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

    fun fillRecommendetRecipesList(recipe_list: List<Recipe>) {
        recipe_list.forEach() { recipe ->
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.recommended_cocktail_list_card, recommendedCocktailList, false)
            recipeCard.cocktail_name.text = recipe.name
            recipeCard.cocktail_rating_bar.rating = recipe.rating
            recipeCard.cocktail_id.text = recipe.id.toString()
            recipeCard.recommendedCocktailsImageView.setOnClickListener { openDetails(recipeCard) }
            recipeCard.recommendedCocktailsConstraintLayout.setOnClickListener { openDetails(recipeCard) }

            /* TODO: recipeCard.cocktail_image */
            recommendedCocktailList.addView(recipeCard)
        }
    }

    fun fillTrendingRecipesList(recipe_list: List<Recipe>) {
        recipe_list.forEach() { recipe ->
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, recipesLayout, false)
            recipeCard.cocktail_name.text = recipe.name
            recipeCard.cocktail_ratings.text = recipe.times_rated.toString()
            recipeCard.cocktail_rating_bar.rating = recipe.rating
            recipeCard.cocktail_difficulty.text =  getRecipeDifficutly(recipe.difficulty)
            val preparationTime: String = recipe.preptime_minutes.toString() + " " +getString(R.string.minutes)
            recipeCard.cocktail_preparationtime.text = preparationTime
            recipeCard.cocktail_id.text = recipe.id.toString()
            recipeCard.imageView.setOnClickListener { openDetails(recipeCard) }
            recipeCard.linearLayout.setOnClickListener { openDetails(recipeCard) }

            /* TODO: recipeCard.cocktail_image */
            recipesLayout.addView(recipeCard)
        }
    }

    fun profilePictureOnClick(view: View){
            val intent = Intent(this, UserSettingsActivity::class.java)
            startActivity(intent)
    }

    fun openDetails(recipeCard : View)
    {
        val intent = Intent(this, CocktailDetailActivity::class.java)
        var bundle = Bundle()

        bundle.putInt("cocktail_id", Integer.valueOf(recipeCard.cocktail_id.text.toString()))

        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun getRecipeDifficutly(difficulty: Int) : String
    {
        when (difficulty) {
            1 -> return resources.getString(R.string.difficulty_very_easy)
            2 -> return resources.getString(R.string.difficulty_easy)
            3 -> return resources.getString(R.string.difficulty_medium)
            4 -> return resources.getString(R.string.difficulty_hard)
            5 -> return resources.getString(R.string.difficulty_very_hard)
        }
        return "Error"
    }
}
