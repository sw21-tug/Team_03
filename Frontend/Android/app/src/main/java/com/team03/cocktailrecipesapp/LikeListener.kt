package com.team03.cocktailrecipesapp

import com.android.volley.Response
import com.google.gson.Gson
import org.json.JSONObject

data class AnswerSuccess(
        var success: Boolean,
        var message: String
)

class LikeListener : Response.Listener<JSONObject> {
    private var onSuccessfullLike : (() -> Unit);

    constructor(_onSuccessfullRegister: (() -> Unit)){
        onSuccessfullLike = _onSuccessfullRegister;
    }

    override fun onResponse(response: JSONObject) {
        val answer = Gson().fromJson(response.toString(), AnswerSuccess::class.java)
        onSuccessfullLike.invoke();
    }
}