package com.team03.cocktailrecipesapp

import com.android.volley.Response
import com.google.gson.Gson
import com.team03.cocktailrecipesapp.ui.login.AnswerUserID
import org.json.JSONObject

class RegisterListener : Response.Listener<JSONObject> {
    private var onSuccessfullRegister : (() -> Unit);

    constructor(_onSuccessfullRegister: (() -> Unit)){
        onSuccessfullRegister = _onSuccessfullRegister;
    }

    override fun onResponse(response: JSONObject) {
        //val answer = Gson().fromJson(response.toString(), AnswerUserID::class.java)
        onSuccessfullRegister.invoke();
    }
}