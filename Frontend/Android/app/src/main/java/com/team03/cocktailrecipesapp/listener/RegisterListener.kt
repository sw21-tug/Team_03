package com.team03.cocktailrecipesapp.listener

import com.android.volley.Response
import org.json.JSONObject

class RegisterListener : Response.Listener<JSONObject> {
    private var onSuccessfullRegister : (() -> Unit);

    constructor(_onSuccessfullRegister: (() -> Unit)){
        onSuccessfullRegister = _onSuccessfullRegister;
    }

    override fun onResponse(response: JSONObject) {
        onSuccessfullRegister.invoke();
    }
}