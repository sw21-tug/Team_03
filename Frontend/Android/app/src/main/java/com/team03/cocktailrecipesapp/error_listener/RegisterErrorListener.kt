package com.team03.cocktailrecipesapp.error_listener

import com.android.volley.Response
import com.android.volley.VolleyError

class RegisterErrorListener : Response.ErrorListener {
    private var onErrorRegister : (() -> Unit);

    constructor(_onErrorRegister: (() -> Unit)){
        onErrorRegister = _onErrorRegister;
    }

    override fun onErrorResponse(error: VolleyError) {
        onErrorRegister.invoke();
    }
}