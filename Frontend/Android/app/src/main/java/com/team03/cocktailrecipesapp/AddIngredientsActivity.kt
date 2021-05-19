package com.team03.cocktailrecipesapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.team03.cocktailrecipesapp.recipes.GetIngredientsErrorListener
import com.team03.cocktailrecipesapp.recipes.GetIngredientsListener
import com.team03.cocktailrecipesapp.recipes.Ingredient
import kotlinx.android.synthetic.main.error_msg_indicator.*
import kotlinx.android.synthetic.main.ingredients_item.view.*
import kotlinx.android.synthetic.main.ingredients_list.*
import kotlinx.android.synthetic.main.progress_indicator.*
import java.util.*

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
        progressbar.visibility = View.GONE
        ingredients.forEach { ingredient ->
            val ingredientView = LayoutInflater.from(this).inflate(R.layout.ingredients_item, ingredientsList, false)
            ingredientView.ingredientItemCheckbox.text = ingredient.name
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
        val picked_ingredients_amount: MutableList<Int> = mutableListOf()
        val picked_ingredients_unit: MutableList<String> = mutableListOf()
        ingredientsList.forEach { ingredient ->
            if(ingredient.ingredientItemCheckbox.isChecked)
            {
                // TODO: check if amount is not empty
                if (ingredient.etIngredientAmount.text.toString().isEmpty() || ingredient.etIngredientUnit.text.toString().isEmpty())
                {
                    Toast.makeText(applicationContext, resources.getString(R.string.ingredient_or_Unit_has_no_amount), Toast.LENGTH_LONG).show()
                    return
                }

                val ingredientName = ingredient.ingredientItemCheckbox.text.toString()
                val ingredientAmount = Integer.valueOf(ingredient.etIngredientAmount.text.toString());
                val ingredientUnit = ingredient.etIngredientUnit.text.toString();

                picked_ingredients.add(ingredientName)
                picked_ingredients_amount.add(ingredientAmount)
                picked_ingredients_unit.add(ingredientUnit)
            }
        }

        // Send result of selected ingredients back to previous activity
        val intent = Intent()
        intent.putStringArrayListExtra ("pickedingredients", picked_ingredients as ArrayList<String>)
        intent.putIntegerArrayListExtra ("pickedingredientsamount", picked_ingredients_amount as ArrayList<Int>)
        intent.putStringArrayListExtra ("pickedingredientsunit", picked_ingredients_unit as ArrayList<String>)
        setResult(RESULT_OK, intent)
        finish()
    }
}