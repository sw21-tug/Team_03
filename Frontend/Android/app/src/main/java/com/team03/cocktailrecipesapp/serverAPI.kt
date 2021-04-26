package com.team03.cocktailrecipesapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HttpResponse
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/*
* HowTo: Create object of class. To communicate with the server just call one of the methods. Pass the informations.
* Important part is, that you have to create the Listeners before calling the methods so you can handle the response yourself.
* Request will be send by the methods.
 */
class serverAPI(context: Context)
{
    val context_ = context


    fun register(username: String, password_hash: String, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val register_json = """
            {
                "name": """" + username + """",
                "password_hash": """" + password_hash + """"
            }
            """

        val json = JSONObject(register_json)
        return sendRequest(json, listener, error_listener)
    }

    fun login(username: String, password_hash: String, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val login_json = """
            {
                "name": """" + username + """",
                "password_hash": """" + password_hash + """"
            }
            """

        val json = JSONObject(login_json)
        return sendRequest(json, listener, error_listener)
    }

    fun addRecipe(user_id: Int, name: String, preptime_minutes: Int, difficulty: Int, instruction: String, ingredient_names: List<String>, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        var add_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "name": """" + name + """",
                "preptime_minutes": """" + preptime_minutes.toString() + """",
                "difficulty": """" + difficulty.toString() + """",
                "instruction": """" + instruction + """",
                "ingredient_names": [
            """

        for (i in ingredient_names)
        {
            if (i == ingredient_names.last())
            {
                add_recipe_json += "$i]}";
            }
            else
            {
                add_recipe_json += "\"$i\",";
            }
        }

        val json = JSONObject(add_recipe_json)
        return sendRequest(json, listener, error_listener)
    }

    fun rateRecipe(user_id: Int, recipe_id: Int, value: Int, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val rate_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "recipe_id": """" + recipe_id.toString() + """",
                "value": """" + value.toString() + """"
            }
            """

        val json = JSONObject(rate_recipe_json)
        return sendRequest(json, listener, error_listener)
    }

    fun getRecipes(listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val json = JSONObject()
        return sendRequest(json, listener, error_listener)
    }

    fun changePassword(user_id: Int, old_password_hash: String, new_password_hash: String, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val change_password_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "old_password_hash": """" + old_password_hash + """",
                "new_password_hash": """" + new_password_hash + """"
            }
            """

        val json = JSONObject(change_password_json)
        return sendRequest(json, listener, error_listener)
    }

    fun deleteRecipe(user_id: Int, recipe_id: Int, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val delete_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "recipe_id": """" + recipe_id.toString() + """"
            }
            """

        val json = JSONObject(delete_recipe_json)
        return sendRequest(json, listener, error_listener)
    }

    fun likeRecipe(user_id: Int, recipe_id: Int, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val like_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "recipe_id": """" + recipe_id.toString() + """"
            }
            """

        val json = JSONObject(like_recipe_json)
        return sendRequest(json, listener, error_listener)
    }

    private fun sendRequest(jsonObject: JSONObject, listener: Response.Listener<JSONObject>, error_listener: Response.ErrorListener) : Int
    {
        val queue = Volley.newRequestQueue(context_)
        val url = "localhost:3000"

        val request = JsonObjectRequest(url, jsonObject, listener, error_listener)

        queue.add(request)
        queue.start()

        return 0
    }
}