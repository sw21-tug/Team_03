package com.team03.cocktailrecipesapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.team03.cocktailrecipesapp.error_listener.GetRecipesErrorListener
import com.team03.cocktailrecipesapp.listener.GetRecipesListener
import com.team03.cocktailrecipesapp.listener.Recipe
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.recommended_cocktail_list_card.*
import kotlinx.android.synthetic.main.recommended_cocktail_list_card.view.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_id
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_name
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_rating_bar
import java.util.*

class MainActivity : SharedPreferencesActivity() {
    lateinit var recommendedProgressBar: View
    lateinit var trendingProgressBar: View
    lateinit var recipesLayout: LinearLayout
    lateinit var recommendedCocktailList: LinearLayout
    lateinit var txtSeeAll : TextView

    var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            userId = shared.getInt("userId", 0)
            userName = shared.getString("username","").toString()
            println(userName)
        }
        if (userId == 0) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else {
            shared_editor.putInt("userId", userId).apply()
            shared_editor.putString("username", userName).apply()
            setContentView(R.layout.activity_main)
            txtSeeAll = findViewById(R.id.txtViewSeeAll)
            trendingProgressBar = findViewById(R.id.progress_load_trending);
            recommendedProgressBar  = findViewById(R.id.progress_load_recommended);
            recipesLayout = findViewById(R.id.trending_cocktail_list)
            recommendedCocktailList = findViewById(R.id.recommended_cocktail_list)

            txtSeeAll.setOnClickListener() {
                    val intent = Intent(this, SeeAllRecipiesActivity::class.java)
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

        val b = intent.extras
        if (b != null) username = b.getString("username").toString()
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
        recommendedProgressBar.visibility = View.INVISIBLE;
        trendingProgressBar.visibility = View.INVISIBLE;
        recipesLayout.visibility = View.VISIBLE
    }

    fun onFailedGetRecipes() {
        recommendedProgressBar.visibility = View.INVISIBLE;
        trendingProgressBar.visibility = View.INVISIBLE;
        recipesLayout.visibility = View.INVISIBLE
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

    fun fillRecommendetRecipesList(recipe_list: List<Recipe>) {
        recipe_list.forEach() { recipe ->
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.recommended_cocktail_list_card, recommendedCocktailList, false)
            recipeCard.cocktail_name.text = recipe.name
            recipeCard.cocktail_rating_bar.rating = recipe.rating
            recipeCard.cocktail_id.text = recipe.id.toString()
            Picasso.get()
                .load(ImageUrl(recipe.image).url)
                .fit()
                .placeholder(R.drawable.ic_cocktail_image)
                .error(R.drawable.ic_cocktail_image)
                .into(recipeCard.recommendedCocktailsImageView);

            recipeCard.recommendedCocktailsImageView.setOnClickListener { openDetails(recipeCard) }
            recipeCard.recommendedCocktailCard.setOnClickListener { openDetails(recipeCard) }

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
            Picasso.get()
                .load(ImageUrl(recipe.image).url)
                .fit()
                .placeholder(R.drawable.ic_cocktail_image)
                .error(R.drawable.ic_cocktail_image)
                .into(recipeCard.trendingCocktailCardImageView);

            recipeCard.trendingCocktailCardImageView.setOnClickListener { openDetails(recipeCard) }
            recipeCard.trendingCocktailListCardLinearLayout.setOnClickListener { openDetails(recipeCard) }

            /* TODO: recipeCard.cocktail_image */
            recipesLayout.addView(recipeCard)
        }
    }

    fun profilePictureOnClick(view: View) {
        val intent = Intent(this, UserProfileActivity::class.java)
        var bundle = Bundle()
        bundle.putString("username", userName)
        bundle.putInt("_creator_id", userId)
        intent.putExtras(bundle)
        startActivity((intent))
    }

    fun openDetails(recipeCard : View)
    {
        val intent = Intent(this, CocktailDetailActivity::class.java)
        var bundle = Bundle()

        bundle.putInt("cocktail_id", Integer.valueOf(recipeCard.cocktail_id.text.toString()))

        intent.putExtras(bundle)
        startActivity(intent)
    }
}
