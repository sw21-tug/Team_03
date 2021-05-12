package com.team03.cocktailrecipesapp.error_listener

import com.android.volley.Response
import com.android.volley.VolleyError


class LoginErrorListener : Response.ErrorListener {
    private var onFail : (() -> Unit);

    constructor(_onFail: (() -> Unit)){
        onFail = _onFail;
    }

    override fun onErrorResponse(error: VolleyError) {
        onFail.invoke();
    }
}
