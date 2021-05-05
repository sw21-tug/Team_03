package com.team03.cocktailrecipesapp.ui.login

import com.android.volley.Response
import com.google.gson.Gson
import org.json.JSONObject

data class AnswerUserID(
    var user_id: Int,
    var message: String
)

class LoginListener : Response.Listener<JSONObject> {
    private var onSuccess : ((Int) -> Unit);

    constructor(_onSuccess: ((Int) -> Unit)){
        onSuccess = _onSuccess;
    }

    override fun onResponse(response: JSONObject) {
        val answer = Gson().fromJson(response.toString(), AnswerUserID::class.java)
        onSuccess.invoke(answer.user_id);
    }
}