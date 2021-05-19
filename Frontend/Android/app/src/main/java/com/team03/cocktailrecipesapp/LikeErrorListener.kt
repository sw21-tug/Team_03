package com.team03.cocktailrecipesapp

import com.android.volley.Response
import com.android.volley.VolleyError

class LikeErrorListener : Response.ErrorListener {
    private var onErrorLike : (() -> Unit);

    constructor(_onErrorRegister: (() -> Unit)){
        onErrorLike = _onErrorRegister;
    }

    override fun onErrorResponse(error: VolleyError) {
        onErrorLike.invoke();
    }
}