package com.team03.cocktailrecipesapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.team03.cocktailrecipesapp.error_listener.GetRecipesErrorListener
import com.team03.cocktailrecipesapp.listener.GetRecipesListener
import com.team03.cocktailrecipesapp.listener.Recipe
import kotlinx.android.synthetic.main.activity_cocktail_detail.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*
import java.util.ArrayList


class SeeAllRecipiesActivity : SharedPreferencesActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var recipesLayout: LinearLayout
    lateinit var searchBar: SearchView
    var recipe_list_gl: List<Recipe> = listOf()
    var recipe_list_filtered_by_ingredients: List<Recipe> = listOf()
    var ingredients_pub_name: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_see_all_recipies)
        progressBar = findViewById(R.id.progressbar);
        recipesLayout = findViewById(R.id.filtered_cocktail_list)
        searchBar = findViewById(R.id.searchRecipesView)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                return false
            }
            override fun onQueryTextChange(p0: String): Boolean {
                var filtered_recipes: MutableList<Recipe> = recipe_list_filtered_by_ingredients.toMutableList()
                var values: MutableList<Boolean> = mutableListOf()

                filtered_recipes.filter { values.add(it.name.toLowerCase().contains(p0.toLowerCase()))}

                var size:Int = filtered_recipes.size
                var i:Int = 0
                while (i < size)
                {
                    if (values.get(i) == false)
                    {
                        filtered_recipes.removeAt(i)
                        values.removeAt(i)
                        size--
                    }
                    else {
                        i++
                    }
                }
                recipesLayout.removeAllViews()
                fillAllRecipesList(filtered_recipes.toList())
                return false
            }
        })
        getAllRecipesList()
    }

    fun getAllRecipesList() {
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
        recipe_list_gl = recipe_list
        fillAllRecipesList(recipe_list)
        progressBar.visibility = View.INVISIBLE
        recipesLayout.visibility = View.VISIBLE
        recipe_list_filtered_by_ingredients = recipe_list_gl
    }

    fun onFailedGetRecipes() {
        progressBar.visibility = View.INVISIBLE;
        recipesLayout.visibility = View.INVISIBLE
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

    fun checkIfIngredientsInRecipe(recipe: Recipe) : Boolean
    {
        for (i in 0..(ingredients_pub_name.orEmpty().size - 1)) {
                for (k in 0..(recipe.ingredients.size - 1)) {
                    if (recipe.ingredients.toMutableList().get(k).name == ingredients_pub_name.orEmpty().toMutableList().get(i))
                    {
                        return true;
                    }
                }
        }
        return false;
    }

    fun fillAllRecipesList(recipe_list: List<Recipe>) {
        recipesLayout.removeAllViews()
        recipe_list.forEach() { recipe ->
          if((ingredients_pub_name == null || ingredients_pub_name.orEmpty().size == 0)
                || checkIfIngredientsInRecipe(recipe))
            {
              val recipeCard = LayoutInflater.from(this).inflate(R.layout.trending_cocktail_list_card, recipesLayout, false)
              recipeCard.cocktail_name.text = recipe.name
              recipeCard.cocktail_ratings.text = recipe.times_rated.toString()
              recipeCard.cocktail_rating_bar.rating = recipe.rating
              recipeCard.cocktail_difficulty.text =  getRecipeDifficutly(recipe.difficulty)
              val preparationTime: String = recipe.preptime_minutes.toString() + " "+getString(R.string.minutes)
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
    }

    fun addClickListener(recipeCard: View){
        recipeCard.trendingCocktailCardImageView.setOnClickListener { openDetails(recipeCard) }
        recipeCard.trendingCocktailListCardLinearLayout.setOnClickListener { openDetails(recipeCard) }
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


    fun onClickFilterByIngredients(view: View) {
        val intent = Intent(this, FilterIngredientsActivity::class.java)
        if(ingredients_pub_name != null)
        {
            intent.putStringArrayListExtra("checked_ingredients_name", ingredients_pub_name as ArrayList<String>)
        }
        startActivityForResult(intent, RESULT_INGREDIENTS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == RESULT_OK && requestCode == RESULT_INGREDIENTS)
        {
            ingredients_pub_name = data.getStringArrayListExtra("pickedingredientsforfilter")
        }
        fillAllRecipesList(recipe_list_gl);
    }
}

