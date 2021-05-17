package com.team03.cocktailrecipesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.team03.cocktailrecipesapp.error_listener.GetRecipeErrorListener
import com.team03.cocktailrecipesapp.listener.GetRecipeListener
import com.team03.cocktailrecipesapp.listener.Ingrediant
import com.team03.cocktailrecipesapp.listener.RecipeDetail
import kotlinx.android.synthetic.main.activity_cocktail_detail.*
import kotlinx.android.synthetic.main.progress_indicator.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_difficulty
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_name
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_rating_bar
import kotlinx.android.synthetic.main.trending_cocktail_list_card.view.*


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

    var isLiked = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)

        val b = intent.extras
        var recipe_id = -1 // or other values

        if (b != null) recipe_id = b.getInt("cocktail_id")

        setInvisibleWhileLoading()

        getRecipe(recipe_id, b)
    }

    fun getRecipe(recipe_id: Int, bundle: Bundle?) {
        val server = serverAPI(applicationContext)
        val listener =
            GetRecipeListener(::onSuccessfulGetRecipe)
        val errorListener = GetRecipeErrorListener({ (::onFailedGetRecipe)(bundle) })
        server.getRecipe(recipe_id, userId, listener, errorListener)
    }

    @SuppressLint("SetTextI18n")
    fun onSuccessfulGetRecipe(recipe: RecipeDetail) {

        cocktail_name.text = recipe.name
        cocktail_difficulty.text = recipe.difficulty.toString()
        cocktail_rating_bar.rating = recipe.rating
        cocktail_preparation_time.text = recipe.preptime_minutes.toString() + " " + getString(R.string.minutes)
        cocktail_instruction.text = recipe.instruction



        var listView = findViewById<ListView>(R.id.recipe_list_view)
        val adapter = RecipeAdapter(this, recipe.ingredients)
        listView.adapter = adapter

        setVisibleAfterLoading()

    }

    fun onFailedGetRecipe(bundle: Bundle?) {
        println("Failed to get recipe from server!")
        Toast.makeText(this,  "Cannot get any recipe information from server", Toast.LENGTH_LONG).show()
        //txtViewErrorMsg.text = resources.getString(R.string.failed_to_load_recipes_from_server)
        //txtViewErrorMsgContainer.visibility = View.VISIBLE
        //Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_recipes_from_server), Toast.LENGTH_LONG).show()

        cocktail_name.text = bundle?.getString("cocktail_name")
        cocktail_difficulty.text = bundle?.getString("cocktail_difficulty")
        cocktail_rating_bar.rating = bundle?.getFloat("cocktail_rating_bar")!!
        cocktail_preparation_time.text = bundle?.getString("preparation_time") + " " + getString(R.string.minutes)
        cocktail_instruction.text = "No instruction available.."


        setVisibleAfterLoading()
    }

    fun backToMainscreen(view: View)
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun setInvisibleWhileLoading()
    {
        imageView8.visibility = View.INVISIBLE
        cocktail_name.visibility = View.INVISIBLE
        cocktail_difficulty.visibility = View.INVISIBLE
        cocktail_rating_bar.visibility = View.INVISIBLE
        cocktail_preparation_time.visibility = View.INVISIBLE
        cocktail_instruction.visibility = View.INVISIBLE

        prepTimeText.visibility = View.INVISIBLE
        prepTimeText3.visibility = View.INVISIBLE
        ingredients.visibility = View.INVISIBLE


        var listView = findViewById<ListView>(R.id.recipe_list_view)
        listView.visibility = View.INVISIBLE
    }

    fun setVisibleAfterLoading()
    {
        imageView8.visibility = View.VISIBLE
        cocktail_name.visibility = View.VISIBLE
        cocktail_difficulty.visibility = View.VISIBLE
        cocktail_rating_bar.visibility = View.VISIBLE
        cocktail_preparation_time.visibility = View.VISIBLE
        cocktail_instruction.visibility = View.VISIBLE

        prepTimeText.visibility = View.VISIBLE
        prepTimeText3.visibility = View.VISIBLE
        ingredients.visibility = View.VISIBLE

        var listView = findViewById<ListView>(R.id.recipe_list_view)
        listView.visibility = View.VISIBLE

        progressbar.visibility = View.INVISIBLE
    }

    fun onUnsuccessfullLike(){
        //TODO ERROR_MSG
        System.out.println("not successful\n")
    }

    fun onSuccessfullLike(){
        var imgLike = findViewById<ImageButton>(R.id.imageButtonLike);
        if(!isLiked) {
            imgLike.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.heart_filled ));
            isLiked = true
            System.out.println("liked\n")
        } else {
            imgLike.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.hearth_empty ));
            isLiked = false
            System.out.println("disliked\n")
        }

    }

    fun like(user_id: Int, recipe_id: Int): Boolean {
        val server = serverAPI(this)
        val listener = LikeListener(::onSuccessfullLike)
        val error_listener = LikeErrorListener(::onUnsuccessfullLike)
        val answer = server.likeRecipe(user_id, recipe_id,
                listener, error_listener);
        if (answer == 1)
        {
            return false
        }
        return true
    }

    fun likeOnClick(view: View) {
        run {
            System.out.println("onclick\n")
            like(1, 1);
        }
    }



}