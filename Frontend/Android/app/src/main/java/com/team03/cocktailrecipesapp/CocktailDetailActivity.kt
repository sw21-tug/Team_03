package com.team03.cocktailrecipesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.team03.cocktailrecipesapp.dialogs.DeleteRecipeDialogFragment
import com.team03.cocktailrecipesapp.error_listener.DeleteRecipeErrorListener
import com.team03.cocktailrecipesapp.error_listener.GetRecipeErrorListener
import com.team03.cocktailrecipesapp.listener.*
import kotlinx.android.synthetic.main.activity_cocktail_detail.*
import kotlinx.android.synthetic.main.progress_indicator.*
import kotlinx.android.synthetic.main.rating_layout.view.*
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_difficulty
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_name
import kotlinx.android.synthetic.main.trending_cocktail_list_card.cocktail_rating_bar


public interface RatingInterface  {
    fun onSelectedData(rating: Int);


}

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

        // Get unit element
        val ingrediant_unit = rowView.findViewById(R.id.ingrediant_unit) as TextView

        // Get subtitle element
        val ingrediant_name = rowView.findViewById(R.id.ingrediant_name) as TextView

        val ingrediant = getItem(position) as Ingrediant

        ingrediant_name.text = ingrediant.name
        ingrediant_unit.text = ingrediant.unit
        ingrediant_amount.text = ingrediant.amount.toString()

        return rowView
    }

}

class CocktailDetailActivity : AppCompatActivity(), RatingInterface  {

    var recipe_id = -1
    var my_rating = 0
//    lateinit var imgBtnRate : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)
        cocktail_creator_username.setOnClickListener { onRecipeCreatorClick() }

        val b = intent.extras
//        imgBtnRate = findViewById(R.id.imgBtnRate)


        if (b != null) recipe_id = b.getInt("cocktail_id")

        setInvisibleWhileLoading()

        getRecipe(b)
    }

    fun onRecipeCreatorClick() {
        val intent = Intent(this, UserProfileActivity::class.java)
        var bundle = Bundle()
        bundle.putString("username", cocktail_creator_username.text.toString())
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun onSuccessfulDeleteRecipe() {
        onBackPressed();
    }

    fun onFailedDeleteRecipe() {
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_delete_recipe), Toast.LENGTH_LONG).show()
    }

    fun deleteRecipe(view: View)
    {
        var deleteRecipeDialog = DeleteRecipeDialogFragment()
        deleteRecipeDialog.recipe_id = recipe_id
        deleteRecipeDialog.server = serverAPI(applicationContext)
        deleteRecipeDialog.succesListener = DeleteRecipeListener (::onSuccessfulDeleteRecipe)
        deleteRecipeDialog.errorListener = DeleteRecipeErrorListener(::onFailedDeleteRecipe)

        deleteRecipeDialog.show(this.supportFragmentManager, "")
    }

    fun getRecipe(bundle: Bundle?) {
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
        my_rating = recipe.my_rating
        cocktail_creator_username.text = recipe.creator_user

        if (recipe.is_mine == 1)
            delete_recipe_button.visibility = View.VISIBLE;

        var listView = findViewById<ListView>(R.id.recipe_list_view)

        val adapter = RecipeAdapter(this, recipe.ingredients)
        listView.adapter = adapter


        setVisibleAfterLoading()

    }

    fun onFailedGetRecipe(bundle: Bundle?) {
        println("Failed to get recipe from server!")
        Toast.makeText(this,  "Cannot get any recipe information from server", Toast.LENGTH_LONG).show()

        cocktail_name.text = bundle?.getString("cocktail_name")
        cocktail_difficulty.text = bundle?.getString("cocktail_difficulty")
        cocktail_rating_bar.rating = bundle?.getFloat("cocktail_rating_bar")!!
        cocktail_preparation_time.text = bundle?.getString("preparation_time") + " " + getString(R.string.minutes)
        cocktail_instruction.text = "No instruction available.."

        setVisibleAfterLoading()
    }

    fun backToMainscreen(view: View)
    {
        onBackPressed();
    }

    fun setInvisibleWhileLoading()
    {
        imageView8.visibility = View.INVISIBLE
        cocktail_name.visibility = View.INVISIBLE
        cocktail_difficulty.visibility = View.INVISIBLE
        cocktail_rating_bar.visibility = View.INVISIBLE
        cocktail_preparation_time.visibility = View.INVISIBLE
        cocktail_instruction.visibility = View.INVISIBLE
        cocktail_creator_username.visibility = View.INVISIBLE
        cocktail_creator_text.visibility = View.INVISIBLE

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
        cocktail_creator_username.visibility = View.VISIBLE
        cocktail_creator_text.visibility = View.VISIBLE

        prepTimeText.visibility = View.VISIBLE
        prepTimeText3.visibility = View.VISIBLE
        ingredients.visibility = View.VISIBLE

        var listView = findViewById<ListView>(R.id.recipe_list_view)
        listView.visibility = View.VISIBLE

        progressbar.visibility = View.INVISIBLE
    }


    fun rateRecipe(view: View)
    {
        if (my_rating == 0)
        {
            val ratingDialog = RatingDialog()
            ratingDialog.show(supportFragmentManager, "ratingDialog")
        }
        else
        {
            showAlreadyVotedDialog()
        }

    }

    fun onSuccessfulRateRecipe() {
        println("Successfully rated recipe");
    }

    fun onFailedRateRecipe() {
        println("Failed to rate recipe");
    }

    fun rateRecipe(ratingValue: Int) {
        val server = serverAPI(applicationContext)
        val listener =
                RateRecipeListener(::onSuccessfulRateRecipe)
        val errorListener = RateRecipeErrorListener(::onFailedRateRecipe)
        server.rateRecipe(userId, recipe_id, ratingValue, listener, errorListener);

    }

    override fun onSelectedData(rating: Int) {
        rateRecipe(rating);
    }

    fun showAlreadyVotedDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.already_voted_title))
        builder.setMessage(resources.getString(R.string.already_voted_text))

        builder.setNeutralButton(resources.getString(R.string.already_voted_ok), DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}