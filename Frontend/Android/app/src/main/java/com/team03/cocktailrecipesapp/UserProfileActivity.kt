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
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.recommended_cocktail_list_card.view.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_id
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_name
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.cocktail_rating_bar
import java.util.*

class UserProfileActivity : SharedPreferencesActivity() {
    lateinit var userNameText: TextView
    lateinit var userImage: ImageView
    lateinit var logoutTextView : TextView
    lateinit var languageTextView : TextView
    lateinit var backButton: Button
    lateinit var showLikedButton: Button
    lateinit var showOwnedButton: Button
    lateinit var logoutImgButton: ImageButton
    lateinit var swtLanguage: Switch
    lateinit var recipesLayout: LinearLayout
    lateinit var detailsProgressBar: View


    var detail_user_ID: Int = -1

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_user_profile)

        swtLanguage = findViewById(R.id.swtlanguage)

        val language: String? = shared.getString("Language", "")
        swtLanguage.isChecked = language.equals("kv")
        swtLanguage.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                setLanguage("kv")
            } else {
                setLanguage("en")
            }
            onStart()
        }

        languageTextView = findViewById(R.id.textViewLanguage)
        logoutImgButton = findViewById(R.id.logoutButton)
        logoutTextView = findViewById(R.id.txtViewLogout)
        detailsProgressBar = findViewById(R.id.progress_load_details)
        userNameText = findViewById(R.id.user_profile_username)
        userImage = findViewById(R.id.user_profile_image)
        showOwnedButton = findViewById(R.id.own_recipe_button)
        showOwnedButton.setOnClickListener {
            setSelectedButtonColor(liked_state = false, owned_state = true) }

        showLikedButton = findViewById(R.id.liked_recipe_button)
        showLikedButton.setOnClickListener {
            setSelectedButtonColor(liked_state = true, owned_state = false) }

        backButton = findViewById(R.id.user_profile_back_button)
        backButton.setOnClickListener { onBackPressed() }

        recipesLayout = findViewById(R.id.trending_cocktail_list)

        // set username
        val extras = intent.extras
        if (extras != null) {
            userNameText.text = extras.getString("username")
            detail_user_ID = extras.getInt("_creator_id")
            if(userId != extras.getInt("_creator_id")){
                //invisible Logout button, Settings
                logoutImgButton.visibility = View.INVISIBLE
                logoutTextView.visibility = View.INVISIBLE
                swtLanguage.visibility = View.GONE
                languageTextView.visibility = View.GONE
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

        val liked_state: Boolean = shared.getBoolean("liked_button_state", false)
        val owned_state: Boolean = shared.getBoolean("owned_button_state", true)
        setSelectedButtonColor(liked_state = liked_state, owned_state = owned_state)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        languageTextView = findViewById(R.id.textViewLanguage)
        logoutImgButton = findViewById(R.id.logoutButton)
        swtLanguage = findViewById(R.id.swtlanguage)
        logoutTextView = findViewById(R.id.txtViewLogout)
        detailsProgressBar = findViewById(R.id.progress_load_details)
        userNameText = findViewById(R.id.user_profile_username)
        userImage = findViewById(R.id.user_profile_image)
        showOwnedButton = findViewById(R.id.own_recipe_button)
        showLikedButton = findViewById(R.id.liked_recipe_button)
        backButton = findViewById(R.id.user_profile_back_button)
        backButton.setOnClickListener { onBackPressed() }

        recipesLayout = findViewById(R.id.trending_cocktail_list)

        // set profile picture according to selected language
        val language_ = shared.getString("Language", "");
        var avatarImgae = findViewById<ImageButton>(R.id.imgBtnAvatar);
        if (language_.equals("kv")) {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
        } else {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar ));
        }

        // set username
        val extras = intent.extras
        if (extras != null) {
            userNameText.text = extras.getString("username")
            detail_user_ID = extras.getInt("_creator_id")
            if(userId != detail_user_ID){
                println("gone!!")
                //invisible Logout button, Settings
                logoutImgButton.visibility = View.INVISIBLE
                logoutTextView.visibility = View.INVISIBLE
                swtLanguage.visibility = View.GONE
                languageTextView.visibility = View.GONE
            }
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


    fun onSuccessfulGetRecipes(recipe_list: List<Recipe>) {
        fillRecipesList(recipe_list)
        detailsProgressBar.visibility = View.INVISIBLE
    }

    fun onFailedGetRecipes() {
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
        detailsProgressBar.visibility = View.INVISIBLE
    }

    fun fillRecipesList(recipe_list: List<Recipe>) {
        recipesLayout.removeAllViews()

        recipe_list.forEach() { recipe ->
            val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, recipesLayout, false)
            recipeCard.cocktail_name.text = recipe.name
            recipeCard.cocktail_ratings.text = recipe.times_rated.toString()
            recipeCard.cocktail_rating_bar.rating = recipe.rating
            recipeCard.cocktail_difficulty.text =  getRecipeDifficutly(recipe.difficulty)
            val preparationTime: String = recipe.preptime_minutes.toString() + " "+ getString(R.string.minutes)
            recipeCard.cocktail_preparationtime.text = preparationTime
            recipeCard.cocktail_id.text = recipe.id.toString()
            Picasso.get()
                .load(ImageUrl(recipe.image).url)
                .fit()
                .placeholder(R.drawable.ic_cocktail_image)
                .error(R.drawable.ic_cocktail_image)
                .into(recipeCard.trendingCocktailCardImageView);

            addClickListener(recipeCard)

            /* TODO: recipeCard.cocktail_image */

            recipesLayout.addView(recipeCard)
        }
    }

    fun addClickListener(recipeCard: View){
        recipeCard.trendingCocktailCardImageView.setOnClickListener { openDetails(recipeCard) }
        recipeCard.trendingCocktailListCardLinearLayout.setOnClickListener { openDetails(recipeCard) }
    }

    fun openDetails(recipeCard : View)
    {
        val intent = Intent(this, CocktailDetailActivity::class.java)
        val bundle = Bundle()

        bundle.putInt("cocktail_id", Integer.valueOf(recipeCard.cocktail_id.text.toString()))
        bundle.putString("cocktail_name", recipeCard.cocktail_name.text.toString())
        bundle.putString("cocktail_ratings", recipeCard.cocktail_ratings.text.toString())
        bundle.putFloat("cocktail_rating_bar", recipeCard.cocktail_rating_bar.rating)
        bundle.putString("cocktail_difficulty", recipeCard.cocktail_difficulty.text.toString())
        bundle.putString("preparation_time", recipeCard.cocktail_preparationtime.text.toString())

        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun showOwnRecipes()
    {
        val server = serverAPI(applicationContext)
        val listener =
                GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener =
                GetRecipesErrorListener(
                        ::onFailedGetRecipes
                )
        server.GetRecipesByUser(detail_user_ID ,listener, errorListener)
    }

    fun showLikedRecipes()
    {
        val server = serverAPI(applicationContext)
        val listener =
            GetRecipesListener(::onSuccessfulGetRecipes)
        val errorListener =
            GetRecipesErrorListener(
                ::onFailedGetRecipes
            )
        server.GetLikedRecipesByUser(detail_user_ID, listener, errorListener)
    }

    fun setSelectedButtonColor(liked_state: Boolean, owned_state:Boolean)
    {
        if (liked_state && !owned_state)
        {
            showLikedButton.setBackgroundColor(resources.getColor(R.color.color_button_selected))
            showOwnedButton.setBackgroundColor(resources.getColor(R.color.color_button_not_selected))
            shared_editor.putBoolean("liked_button_state", liked_state).apply()
            shared_editor.putBoolean("owned_button_state", owned_state).apply()

            showOwnedButton.isClickable = true
            showLikedButton.isClickable = false
            showLikedRecipes()
        }
        else if (owned_state && !liked_state)
        {
            showOwnedButton.setBackgroundColor(resources.getColor(R.color.color_button_selected))
            showLikedButton.setBackgroundColor(resources.getColor(R.color.color_button_not_selected))
            shared_editor.putBoolean("liked_button_state", liked_state).apply()
            shared_editor.putBoolean("owned_button_state", owned_state).apply()
            showOwnedButton.isClickable = false
            showLikedButton.isClickable = true
            showOwnRecipes()
        }
    }
}