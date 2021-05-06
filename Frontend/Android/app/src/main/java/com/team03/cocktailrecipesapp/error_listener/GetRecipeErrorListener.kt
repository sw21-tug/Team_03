package com.team03.cocktailrecipesapp.error_listener

import com.android.volley.Response
import com.android.volley.VolleyError

class GetRecipeErrorListener : Response.ErrorListener
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
