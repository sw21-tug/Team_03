package com.team03.cocktailrecipesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.team03.cocktailrecipesapp.error_listener.AddRecipeErrorListener
import com.team03.cocktailrecipesapp.listener.AddRecipeListener
import kotlinx.android.synthetic.main.activity_add_recipe.*
import java.util.*

const val RESULT_INGREDIENTS = 10

class AddRecipeActivity : SharedPreferencesActivity() {

    var ingredients_pub_name: List<String>? = null
    var ingredients_pub_amount: List<Int>? = null
    var ingredients_pub_unit: List<String>? = null

    override fun onStart() {
        super.onStart()
        timer_picker_minutes.minValue = 1
        timer_picker_minutes.maxValue = 59
        difficulty_picker.displayedValues = difficultyArray
        difficulty_picker.minValue = 1
        difficulty_picker.maxValue = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
    }

    fun onSuccessfullAddRecipe() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun onUnsuccessfullAddRecipe() {
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_add_recipe), Toast.LENGTH_LONG).show()
    }

    fun onClickManageIngredients(view: View) {
        val intent = Intent(this, AddIngredientsActivity::class.java)
        if(ingredients_pub_name != null && ingredients_pub_amount != null && ingredients_pub_unit != null)
        {
            intent.putStringArrayListExtra("checked_ingredients_name", ingredients_pub_name as ArrayList<String>)
            intent.putIntegerArrayListExtra("checked_ingredients_amount", ingredients_pub_amount as ArrayList<Int>)
            intent.putStringArrayListExtra("checked_ingredients_unit", ingredients_pub_unit as ArrayList<String>)
        }

        startActivityForResult(intent, RESULT_INGREDIENTS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == RESULT_OK && requestCode == RESULT_INGREDIENTS)
        {
            ingredients_pub_name = data.getStringArrayListExtra("pickedingredients")
            ingredients_pub_amount = data.getIntegerArrayListExtra("pickedingredientsamount")
            ingredients_pub_unit = data.getStringArrayListExtra("pickedingredientsunit")
        }
    }

    fun onClickAddRecipe(view: View) {
        val server = serverAPI(applicationContext)
        val listener = AddRecipeListener(::onSuccessfullAddRecipe)
        val errorListener = AddRecipeErrorListener(::onUnsuccessfullAddRecipe)

        val recipe_name: String = etRecipeName.text.toString()
        val recipe_description: String = etRecipeDescription.text.toString()
        val preperation_time: Int = timer_picker_minutes.value
        val difficulty: Int = difficulty_picker.value
        val image: String = cocktail_image_url.text.toString()

        if (ingredients_pub_name.orEmpty().isEmpty() || ingredients_pub_amount.orEmpty().isEmpty() || ingredients_pub_unit.orEmpty().isEmpty())
        {
            Toast.makeText(applicationContext, resources.getString(R.string.no_ingredients_selected), Toast.LENGTH_LONG).show()
        }
        else
        {
            server.addRecipe(
                userId,
                recipe_name,
                preperation_time,
                difficulty,
                recipe_description,
                ingredients_pub_name.orEmpty(),
                ingredients_pub_amount.orEmpty(),
                ingredients_pub_unit.orEmpty(),
                image,
                listener,
                errorListener
            )
        }
    }

    fun backToMainscreen(view: View)
    {
        onBackPressed();
    }
}
