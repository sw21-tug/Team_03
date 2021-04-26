package com.team03.cocktailrecipesapp

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime


/**
 * Local unit test
 * checking for correct parsing of the json strings recieved from server
 */
class serverAPIUnitTest {

    private val server = serverAPI();

    data class AnswerSuccess(
            var success: Int? = -1
    )

    data class AnswerUserID(
            var user_id: Int? = -1
    )

    data class Recipe(
            var name: String? = null,
            var preptime_minutes: Int,
            var difficulty: Int,
            var instruction: String? = null,
            var creation_time: LocalDateTime? = null,
            var creator_username: String? = null
    )

    data class AnswerRecipes(
            var recipes: List<Recipe>
    )

    @Test
    fun register_checkAPI_call() {
        val string = server.register("username", "password_hash");
        val answer = Gson().fromJson(string, AnswerSuccess::class.java);

        Assert.assertNotNull(answer)
        Assert.assertEquals(0, answer.success)
    }

    fun login_checkAPI_call() {
        val string = server.login("username", "password_hash");
        val answer = Gson().fromJson(string, AnswerUserID::class.java);

        Assert.assertNotNull(answer)
        Assert.assertEquals(0, answer.user_id)
    }

    fun getRecipes_checkAPI_call() {
        val string = server.getRecipes();
        val answer = Gson().fromJson(string, AnswerRecipes::class.java);

        Assert.assertNotNull(answer)
        Assert.assertTrue(answer.recipes.isNotEmpty())

        for (recipe in answer.recipes)
        {
            Assert.assertNotNull(recipe.creation_time);
            Assert.assertNotNull(recipe.creator_username);
            Assert.assertNotNull(recipe.instruction);
            Assert.assertNotNull(recipe.name);

            Assert.assertTrue(recipe.difficulty > 0)
            Assert.assertTrue(recipe.difficulty <= 10)
        }
    }

    fun changePassword_checkAPI_call() {
        val string = server.changePassword(-1, "old_password_hash", "new_password_hash");
        val answer = Gson().fromJson(string, AnswerSuccess::class.java);

        Assert.assertNotNull(answer)
        Assert.assertEquals(0, answer.success)
    }

    fun deleteRecipe_checkAPI_call() {
        val string = server.deleteRecipe(-1, -1);
        val answer = Gson().fromJson(string, AnswerSuccess::class.java);

        Assert.assertNotNull(answer)
        Assert.assertEquals(0, answer.success)
    }

    fun likeRecipe_checkAPI_call() {
        val string = server.likeRecipe(-1, -1);
        val answer = Gson().fromJson(string, AnswerSuccess::class.java);

        Assert.assertNotNull(answer)
        Assert.assertEquals(0, answer.success)
    }

    fun addRecipe_checkAPI_call() {
        val string = server.addRecipe(1, "Freddy Krueger", 5, 1,
                "First put in Stroh80 in 1 2cl shot glass. Take a teaspoon and " +
                        "layer the Jaeger onto the rum. Then finally do the same with the " +
                        "Baileys. Enjoy!", listOf("Stroh80", "Jaegermeister", "Baileys"));
        val answer = Gson().fromJson(string, AnswerSuccess::class.java);

        Assert.assertNotNull(answer)
        Assert.assertTrue(answer.success == 0 || answer.success == 1)
    }

    fun rateRecipe_checkAPI_call() {
        val string = server.likeRecipe(-1, -1);
        val answer = Gson().fromJson(string, AnswerSuccess::class.java);

        Assert.assertNotNull(answer)
        Assert.assertEquals(0, answer.success)
    }
}