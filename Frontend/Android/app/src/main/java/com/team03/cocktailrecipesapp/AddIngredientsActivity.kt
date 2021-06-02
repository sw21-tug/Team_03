package com.team03.cocktailrecipesapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.team03.cocktailrecipesapp.dialogs.AddIngredientDialog
import com.team03.cocktailrecipesapp.recipes.GetIngredientsErrorListener
import com.team03.cocktailrecipesapp.recipes.GetIngredientsListener
import com.team03.cocktailrecipesapp.recipes.Ingredient
import kotlinx.android.synthetic.main.error_msg_indicator.*
import kotlinx.android.synthetic.main.ingredients_item.view.*
import kotlinx.android.synthetic.main.ingredients_list.*
import kotlinx.android.synthetic.main.progress_indicator.*
import java.util.*

public interface IngredientInterface{
    fun onSelectedData(name: String)
}

class AddIngredientsActivity : AppCompatActivity(), IngredientInterface {

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

    fun wasIngredientAlreadySelected(ingredient_name : String, ingredients_names: List<String>) : Int {
        var id : Int = -1;
        var i : Int = 0;
        ingredients_names.forEach { ingredient_name_list ->
            if (ingredient_name_list == ingredient_name)
            {
                id = i;
            }
            i++
        }
        return id;
    }

    fun onSuccessfulGetIngredient(ingredients: List<Ingredient>) {
        val checkeded_ingredients_name = intent.getStringArrayListExtra("checked_ingredients_name")
        val checkeded_ingredients_amount = intent.getIntegerArrayListExtra("checked_ingredients_amount")
        val checkeded_ingredients_unit = intent.getStringArrayListExtra("checked_ingredients_unit")

        progressbar.visibility = View.GONE
        if(checkeded_ingredients_name != null && checkeded_ingredients_amount!= null && checkeded_ingredients_unit != null)
        {
            ingredients.forEach { ingredient ->
                val ingredientView = LayoutInflater.from(this).inflate(R.layout.ingredients_item, ingredientsList, false)
                ingredientView.ingredientItemCheckbox.text = ingredient.name
                val id = wasIngredientAlreadySelected(ingredient.name, checkeded_ingredients_name)
                if (id != -1)
                {
                    ingredientView.ingredientItemCheckbox.isChecked = true
                    ingredientView.etIngredientAmount.setText(checkeded_ingredients_amount?.get(id).toString())
                    ingredientView.etIngredientUnit.setText(checkeded_ingredients_unit.get(id))
                }
                ingredientsList.addView(ingredientView)
            }
        }
        else
        {
            ingredients.forEach { ingredient ->
                val ingredientView = LayoutInflater.from(this).inflate(R.layout.ingredients_item, ingredientsList, false)
                ingredientView.ingredientItemCheckbox.text = ingredient.name
                ingredientsList.addView(ingredientView)
            }
        }

    }

    fun onFailedGetIngredient() {
        txtViewErrorMsg.text = resources.getString(R.string.failed_to_load_ingredients_from_server)
        txtViewErrorMsgContainer.visibility = View.VISIBLE
        Toast.makeText(applicationContext, resources.getString(R.string.failed_to_load_ingredients_from_server), Toast.LENGTH_LONG).show()

        // TODO: get ingredients from shared preferences
    }

    fun onClickChooseIngredients(view: View) {
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

    fun onClickAddIngredient(view: View) {
        val addIngredientDialog = AddIngredientDialog()
        addIngredientDialog.show(supportFragmentManager, "addIngredientDialog")
    }

    override fun onSelectedData(name: String) {

        ingredientsList.forEach { ingredientView ->

            if(ingredientView.ingredientItemCheckbox.text == name)
            {
                ingredientView.ingredientItemCheckbox.isChecked = true
                return
            }
        }

        val ingredientView = LayoutInflater.from(this).inflate(R.layout.ingredients_item, ingredientsList, false)
        ingredientView.ingredientItemCheckbox.text = name
        ingredientView.ingredientItemCheckbox.isChecked = true
        ingredientView.etIngredientAmount.setText("")
        ingredientView.etIngredientUnit.setText("")
        ingredientsList.addView(ingredientView)
    }
}