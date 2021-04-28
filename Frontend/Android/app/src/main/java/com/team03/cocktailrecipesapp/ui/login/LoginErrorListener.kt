package com.team03.cocktailrecipesapp.ui.login

import com.android.volley.Response
import com.android.volley.VolleyError


class LoginErrorListener : Response.ErrorListener {
    override fun onErrorResponse(error: VolleyError) {
        assert(false) //TODO add error handling
    }
}
