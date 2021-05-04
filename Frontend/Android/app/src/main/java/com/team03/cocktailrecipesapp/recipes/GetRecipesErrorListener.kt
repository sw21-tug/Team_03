package com.team03.cocktailrecipesapp.recipes

import com.android.volley.Response
import com.android.volley.VolleyError

class GetRecipesErrorListener : Response.ErrorListener
{
    private var onFailure: () -> Unit

    constructor(_onFailure: () -> Unit)
    {
        onFailure = _onFailure
    }

    override fun onErrorResponse(error: VolleyError?) {
        onFailure.invoke()
    }
}
