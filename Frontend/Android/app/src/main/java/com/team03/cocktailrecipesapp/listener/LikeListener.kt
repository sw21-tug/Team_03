package com.team03.cocktailrecipesapp.listener

import com.android.volley.Response
import com.google.gson.Gson
import org.json.JSONObject

data class AnswerSuccess(
        var success: Boolean,
        var message: String
)

class LikeListener : Response.Listener<JSONObject> {
    private var onSuccessfullLike : (() -> Unit);

    constructor(_onSuccessfullLike: (() -> Unit)){
        onSuccessfullLike = _onSuccessfullLike;
    }

    override fun onResponse(response: JSONObject) {
        val answer = Gson().fromJson(response.toString(), AnswerSuccess::class.java)
        onSuccessfullLike.invoke();
    }
}