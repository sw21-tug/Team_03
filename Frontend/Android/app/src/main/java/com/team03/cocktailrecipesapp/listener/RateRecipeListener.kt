package com.team03.cocktailrecipesapp.listener

import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import org.json.JSONObject



class RateRecipeListener : Response.Listener<JSONObject>
{
    private var onSuccess: (() -> Unit)

    constructor(_onSuccess: () -> Unit)
    {
        onSuccess = _onSuccess
    }

    override fun onResponse(response: JSONObject)
    {
        onSuccess.invoke()
    }
}

