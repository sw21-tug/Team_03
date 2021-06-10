package com.team03.cocktailrecipesapp.listener

import com.android.volley.Response
import org.json.JSONObject

class AddRecipeListener : Response.Listener<JSONObject> {
    private var onSuccessfullAddRecipe : (() -> Unit);

    constructor(_onSuccessfullAddRecipe: (() -> Unit)){
        onSuccessfullAddRecipe = _onSuccessfullAddRecipe;
    }

    override fun onResponse(response: JSONObject) {
        onSuccessfullAddRecipe.invoke();
    }
}
