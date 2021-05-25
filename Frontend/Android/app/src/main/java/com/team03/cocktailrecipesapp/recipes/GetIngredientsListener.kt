package com.team03.cocktailrecipesapp.recipes

import com.android.volley.Response
import com.google.gson.Gson
import org.json.JSONObject

data class Ingredient(
    var name: String
)

data class IngredientList(
    var ingredients: List<Ingredient>
)

class GetIngredientsListener : Response.Listener<JSONObject>
{
    private var onSuccess: ((List<Ingredient>) -> Unit)

    constructor(_onSuccess: (List<Ingredient>) -> Unit)
    {
        onSuccess = _onSuccess
    }

    override fun onResponse(response: JSONObject)
    {
        val serverResponse = Gson().fromJson(response.toString(), IngredientList::class.java)
        onSuccess.invoke(serverResponse.ingredients)
    }
}