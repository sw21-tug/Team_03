package com.team03.cocktailrecipesapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.team03.cocktailrecipesapp.error_listener.GetRecipeErrorListener
import com.team03.cocktailrecipesapp.listener.GetRecipeListener
import com.team03.cocktailrecipesapp.listener.Ingrediant
import com.team03.cocktailrecipesapp.listener.RecipeDetail
import kotlinx.android.synthetic.main.activity_cocktail_detail.cocktail_preparation_time
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_difficulty
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_name
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_rating_bar


class RecipeAdapter(private val context: Context,
                    private val dataSource: List<Ingrediant>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getCount(): Int {
        return dataSource.size
    }


    override fun getItem(position: Int): Any {
        return dataSource[position]
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // Get view for row item
        val rowView = inflater.inflate(R.layout.cocktail_detail_ingrediant_card, parent, false)

        // Get title element
        val ingrediant_amount = rowView.findViewById(R.id.ingrediant_amount) as TextView

        // Get subtitle element
        val ingrediant_name = rowView.findViewById(R.id.ingrediant_name) as TextView

        val recipe = getItem(position) as Ingrediant

        ingrediant_name.text = recipe.name
        ingrediant_amount.text = recipe.amount.toString()

        return rowView
    }

}

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
        val listener =
            GetRecipeListener(::onSuccessfulGetRecipe)
        val errorListener = GetRecipeErrorListener(::onFailedGetRecipe)
        server.getRecipe(recipe_id, userId, listener, errorListener)
    }

    fun onSuccessfulGetRecipe(recipe: RecipeDetail) {

        cocktail_name.text = recipe.name
        cocktail_difficulty.text = recipe.difficulty.toString()
        cocktail_rating_bar.rating = recipe.rating
        cocktail_preparation_time.text = recipe.preptime_minutes.toString()


        var listView = findViewById<ListView>(R.id.recipe_list_view)
        val adapter = RecipeAdapter(this, recipe.ingredients)
        listView.adapter = adapter




    }

    fun onFailedGetRecipe() {
        println("Failed to get recipe from server!")
        //txtViewErrorMsg.text = resources.getString(R.string.failed_to_load_recipes_from_server)
        //txtViewErrorMsgContainer.visibility = View.VISIBLE
        //Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()
    }

}