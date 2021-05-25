package com.team03.cocktailrecipesapp.error_listener

import com.android.volley.Response
import com.android.volley.VolleyError

class AddRecipeErrorListener : Response.ErrorListener {
    private var onErrorAddRcipe : (() -> Unit);

    constructor(_onErrorAddRecipe: (() -> Unit)){
        onErrorAddRcipe = _onErrorAddRecipe;
    }

    override fun onErrorResponse(error: VolleyError) {
        onErrorAddRcipe.invoke();
    }
}