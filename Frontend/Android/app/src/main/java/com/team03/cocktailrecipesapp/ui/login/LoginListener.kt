package com.team03.cocktailrecipesapp.ui.login

import com.android.volley.Response
import com.google.gson.Gson
import org.json.JSONObject

data class AnswerUserID(
    var user_id: Int,
    var message: String
)

class LoginListener : Response.Listener<JSONObject?> {

    private var onSuccess : (() -> Unit)? = null

    constructor(function: (() -> Unit)?){
        onSuccess = function;
    }

    private var user_id = 0;
    private var message = "";
    override fun onResponse(response: JSONObject?) {
        val answer = Gson().fromJson(response.toString(), AnswerUserID::class.java)
        user_id = answer.user_id;
        message = answer.message;
        onSuccess();
    }

    fun getUserId() : Int {
        return user_id;
    }

    fun getMessage() : String {
        return message;
    }
}