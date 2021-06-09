package com.team03.cocktailrecipesapp.listener

import com.android.volley.Response
import com.google.gson.Gson
import org.json.JSONObject

data class Recipe(
    var id: Int,
    var name: String,
    var preptime_minutes: Int,
    var difficulty: Int,
    var instruction: String,
    var creation_time: String,
    var creator_username: String,
    var rating: Float,
    var times_rated: Int,
    var image: String
) { override fun toString(): String {
        return "RecipeTest(id='$id', name='$name', preptime_minutes=$preptime_minutes, difficulty=$difficulty," +
                "instruction='$instruction', creation_time='$creation_time'," +
                "creator_username='$creator_username'," + "rating=$rating, times_rated=$times_rated)"
    }
}

data class RecipeList(
    var recipes: List<Recipe>
)

class GetRecipesListener : Response.Listener<JSONObject>
{
    private var onSuccess: ((List<Recipe>) -> Unit)

    constructor(_onSuccess: (List<Recipe>) -> Unit)
    {
        onSuccess = _onSuccess
    }

    override fun onResponse(response: JSONObject)
    {
        val serverResponse = Gson().fromJson(response.toString(), RecipeList::class.java)
        onSuccess.invoke(serverResponse.recipes)
    }
}
