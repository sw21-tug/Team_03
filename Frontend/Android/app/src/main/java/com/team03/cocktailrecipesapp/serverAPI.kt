package com.team03.cocktailrecipesapp

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class serverAPI
{
    fun register(username: String, password_hash: String) : String
    {
        val register_json = """
            {
                "name": """" + username + """",
                "password_hash": """" + password_hash + """"
            }
            """;

        return sendAndReceive(register_json);
    }

    fun login(username: String, password_hash: String) : String
    {
        val login_json = """
            {
                "name": """" + username + """",
                "password_hash": """" + password_hash + """"
            }
            """;

        return sendAndReceive(login_json);
    }

    fun addRecipe(user_id: Int, name: String, preptime_minutes: Int, difficulty: Int, instruction: String, ingredient_names: List<String>) : String
    {
        var add_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "name": """" + name + """",
                "preptime_minutes": """" + preptime_minutes.toString() + """",
                "difficulty": """" + difficulty.toString() + """",
                "instruction": """" + instruction + """",
                "ingredient_names": [
            """;

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

        return sendAndReceive(add_recipe_json);
    }

    fun rateRecipe(user_id: Int, recipe_id: Int, value: Int) : String
    {
        val rate_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "recipe_id": """" + recipe_id.toString() + """",
                "value": """" + value.toString() + """"
            }
            """;

        return sendAndReceive(rate_recipe_json);
    }

    fun getRecipes() : String
    {
        val get_recipe_json = """
            {
            }
            """;

        return sendAndReceive(get_recipe_json);
    }

    fun changePassword(user_id: Int, old_password_hash: String, new_password_hash: String) : String
    {
        val change_password_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "old_password_hash": """" + old_password_hash + """",
                "new_password_hash": """" + new_password_hash + """"
            }
            """;

        return sendAndReceive(change_password_json);
    }

    fun deleteRecipe(user_id: Int, recipe_id: Int) : String
    {
        val delete_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "recipe_id": """" + recipe_id.toString() + """"
            }
            """;

        return sendAndReceive(delete_recipe_json);
    }

    fun likeRecipe(user_id: Int, recipe_id: Int) : String
    {
        val like_recipe_json = """
            {
                "user_id": """" + user_id.toString() + """",
                "recipe_id": """" + recipe_id.toString() + """"
            }
            """;

        return sendAndReceive(like_recipe_json);
    }

    private fun sendAndReceive(buffer: String) : String
    {
        /*
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "localhost:5000"

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    textView.text = "Response is: ${response.substring(0, 500)}"
                },
                Response.ErrorListener { textView.text = "That didn't work!" })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
*/
        send(buffer);
        return receive();
    }

    private fun send(buffer: String)
    {

    }

    private fun receive() : String
    {
        return "";
    }
}