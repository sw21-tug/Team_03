package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.size
import com.team03.cocktailrecipesapp.error_listener.GetRecipesErrorListener
import com.team03.cocktailrecipesapp.listener.GetRecipesListener
import com.team03.cocktailrecipesapp.listener.Ingrediant
import com.team03.cocktailrecipesapp.listener.Recipe
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*
import java.util.*




class UserProfileActivity : SharedPreferencesActivity() {
    lateinit var userNameText: TextView
    lateinit var userImage: ImageView
    lateinit var backButton: Button
    var userNameExtra: String? = null

    lateinit var swtLangauge: Switch
    lateinit var recipesLayout: LinearLayout
    var detail_user_ID: Int = -1

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_user_profile)

        swtLangauge = findViewById(R.id.swtlanguage)

        val language: String? = shared.getString("Language", "")
        swtLangauge.isChecked = language.equals("kv")
        swtLangauge.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                setLanguage("kv")
            } else {
                setLanguage("en")
            }
            onStart()
        }

        userNameText = findViewById(R.id.user_profile_username)
        userImage = findViewById(R.id.user_profile_image)
        backButton = findViewById(R.id.user_profile_back_button)
        backButton.setOnClickListener { onBackPressed() }

        recipesLayout = findViewById(R.id.trending_cocktail_list)

        // set username
        val extras = intent.extras
        if (extras != null)
            userNameExtra = extras.getString("username")

        userNameText.setText(userNameExtra)

        own_recipe_button.setBackgroundColor(0xFF6200EE.toInt())
        liked_recipe_button.setBackgroundColor(0xFF3700B3.toInt())
        if (extras != null) {
            detail_user_ID = extras.getInt("_creator_id")
            if(userId != extras.getInt("_creator_id")){
                //invisible Logout button, Settings
                logoutButton.visibility = View.INVISIBLE
                swtlanguage.visibility = View.INVISIBLE
                textView.visibility = View.INVISIBLE
            }
        }

        // set profile picture according to selected language
        val language_ = shared.getString("Language", "");
        var avatarImgae = findViewById<ImageButton>(R.id.imgBtnAvatar);
        if (language_.equals("kv")) {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
        } else {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar ));
        }

        val server = serverAPI(applicationContext)
        val listener = GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener = GetRecipesErrorListener(::onFailedGetRecipes)

            server.GetRecipesByUser(detail_user_ID ,listener, errorListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_profile)
        userNameText = findViewById(R.id.user_profile_username)
        userImage = findViewById(R.id.user_profile_image)
        backButton = findViewById(R.id.user_profile_back_button)
        backButton.setOnClickListener { onBackPressed() }

        // set username
        val extras = intent.extras
        if (extras != null)
            userNameExtra = extras.getString("username")
        
        userNameText.setText(userNameExtra)

        // set profile picture according to selected language
        val language_ = shared.getString("Language", "");
        var avatarImgae = findViewById<ImageButton>(R.id.imgBtnAvatar);
        if (language_.equals("kv")) {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
        } else {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar ));
        }
    }

    fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        shared_editor.putString("Language", language).apply()
    }

    fun onLogoutPressed(view: View) {
        shared_editor.putInt("userId", 0).apply()
        userId = 0
        onBackPressed()
    }

    fun showLikedRecipes(view: View)
    {
        val server = serverAPI(applicationContext)
        val listener =
                GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener =
                GetRecipesErrorListener(
                        ::onFailedGetRecipes
                )
        server.GetLikedRecipesByUser(detail_user_ID ,listener, errorListener)

        own_recipe_button.setBackgroundColor(0xFF3700B3.toInt())
        liked_recipe_button.setBackgroundColor(0xFF6200EE.toInt())
    }

    fun onSuccessfulGetRecipes(recipe_list: List<Recipe>) {
        fillLikedRecipesList(recipe_list)
    }

    fun onFailedGetRecipes() {
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

    fun fillLikedRecipesList(recipe_list: List<Recipe>) {

        recipesLayout.removeAllViews()

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

    fun showOwnRecipes(view: View)
    {
        val server = serverAPI(applicationContext)
        val listener =
                GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener =
                GetRecipesErrorListener(
                        ::onFailedGetRecipes
                )
        server.GetRecipesByUser(detail_user_ID ,listener, errorListener)

        own_recipe_button.setBackgroundColor(0xFF6200EE.toInt())
        liked_recipe_button.setBackgroundColor(0xFF3700B3.toInt())
    }
}