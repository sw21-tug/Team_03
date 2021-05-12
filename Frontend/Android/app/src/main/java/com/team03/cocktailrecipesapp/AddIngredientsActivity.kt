package com.team03.cocktailrecipesapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.team03.cocktailrecipesapp.recipes.GetIngredientsErrorListener
import com.team03.cocktailrecipesapp.recipes.GetIngredientsListener
import com.team03.cocktailrecipesapp.recipes.Ingredient
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.error_msg_indicator.*
import kotlinx.android.synthetic.main.ingredients_item.view.*
import kotlinx.android.synthetic.main.ingredients_list.*
import kotlinx.android.synthetic.main.progress_indicator.*
import kotlinx.android.synthetic.main.trending_cocktail_list.*
import java.util.ArrayList

class AddIngredientsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredients_dialog)

        // Fetch ingredients from database
        getIngredients()
    }

    fun getIngredients() {
        val server = serverAPI(applicationContext)
        val listener = GetIngredientsListener(::onSuccessfulGetIngredient)
        val errorListener = GetIngredientsErrorListener(::onFailedGetIngredient)
        server.getIngredients(listener, errorListener)
    }

    fun onSuccessfulGetIngredient(ingredients: List<Ingredient>) {
        progressBar.visibility = View.GONE
        ingredients.forEach { ingredient ->
            val ingredientView = LayoutInflater.from(this).inflate(R.layout.ingredients_item, ingredientsList, false)
            ingredientView.ingredientItem.text = ingredient.name
            ingredientsList.addView(ingredientView)
        }
    }

    fun onFailedGetIngredient() {
        txtViewErrorMsg.text = resources.getString(R.string.failed_to_load_ingredients_from_server)
        txtViewErrorMsgContainer.visibility = View.VISIBLE
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_ingredients_from_server), Toast.LENGTH_LONG).show()

        // TODO: get ingredients from shared preferences
    }

    fun onClickAddIngredients(view: View) {
        val picked_ingredients: MutableList<String> = mutableListOf()
        ingredientsList.forEach { ingredient ->
            if(ingredient.ingredientItem.isChecked)
            {
                picked_ingredients.add(ingredient.ingredientItem.text.toString())
            }
        }
        // Send result of selected ingredients back to previous activity
        val intent = Intent()
        intent.putStringArrayListExtra ("pickedingredients", picked_ingredients as ArrayList<String>)
        setResult(RESULT_OK, intent)
        finish()
    }
}