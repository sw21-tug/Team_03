package com.team03.cocktailrecipesapp.listener

import com.android.volley.Response
import org.json.JSONObject

class DeleteRecipeListener : Response.Listener<JSONObject>
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