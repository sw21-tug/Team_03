package com.team03.cocktailrecipesapp.listener

import com.android.volley.Response
import com.google.gson.Gson
import org.json.JSONObject


data class Ingrediant(
    var id: Int,
    var name: String,
    var amount: Int,
    var unit: String
)

data class RecipeResponse(
    var recipe: RecipeDetail
)

data class RecipeDetail(
    var name: String,
    var preptime_minutes: Int,
    var difficulty: Int,
    var instruction: String,
    var ingredients: List<Ingrediant>,
    var creation_time: String,
    var creator_user: String,
    var creator_id: Int,
    var liked: Int,
    var is_mine: Int,
    var my_rating: Int,
    var rating: Float
)
// Response { recipe: [ { name: <str>, preptime_minutes: <int>, difficulty: <int>, instruction: <str>,
   // ingredients: [ { id: <int>, name: <str>, amount: <int> } ], creation_time: <datetime>, creator_username: <str>,
   // creator_id: <int>, liked: <int>, (0 or 1) is_mine: <int>, (0 or 1), my_rating: <int> (0 = not rated, 1-5) rating, <float> } ] }
{
    override fun toString(): String {
        return "RecipeDetail(name='$name', preptime_minutes=$preptime_minutes, " +
                "difficulty=$difficulty, instruction='$instruction', ingredients=$ingredients,  " +
                "creation_time=$creation_time, creator_username='$creator_user', creator_id=$creator_id," +
                " liked=$liked, is_mine=$is_mine, my_rating=$my_rating, rating=$rating)"
    }
}


class GetRecipeListener : Response.Listener<JSONObject>
{
    private var onSuccess: ((RecipeDetail) -> Unit)

    constructor(_onSuccess: (RecipeDetail) -> Unit)
    {
        onSuccess = _onSuccess
    }

    override fun onResponse(response: JSONObject)
    {
        val serverResponse = Gson().fromJson(response.toString(), RecipeResponse::class.java)
        onSuccess.invoke(serverResponse.recipe)
    }
}
