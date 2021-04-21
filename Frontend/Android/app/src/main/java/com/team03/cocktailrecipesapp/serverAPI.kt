package com.team03.cocktailrecipesapp

import java.time.LocalDateTime

class serverAPI
{
    fun register(username: String, password_hash: String) : String
    {
        val register_json = """
            {
                "name": """" + username + """",
                "password_hash": """" + password_hash + """"
            }
            """

        val register_response = """
           {
               "success": "1"  
           }
        """

        return register_response;
    }

    fun login(username: String, password_hash: String) : String
    {
        // login
        return "";
    }

    fun addRecipe(name: String, preptime_minutes: Int, difficulty: Int, instruction: String, ingredient_names: Array<String>) : String
    {
        // add new recipe
        return "";
    }

    fun rateRecipe(user_id: Int, recipe_id: Int, value: Int) : String
    {
        // rate a recipe
        return "";
    }

    fun getRecipe() : String
    {
        // get existing recipe
        return "";
    }

    fun changePassword(old_password_hash: String, new_password_hash: String) : String
    {
        // change current password
        return "";
    }

    fun deleteRecipe(user_id: Int, recipe_id: Int) : String
    {
        return "";
    }

    fun likeRecipe(user_id: Int, recipe_id: Int) : String
    {
        // like existing recipe
        return "";
    }

    private fun send()
    {

    }

    private fun receive()
    {

    }
}