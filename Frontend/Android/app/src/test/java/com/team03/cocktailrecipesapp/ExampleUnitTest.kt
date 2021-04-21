package com.team03.cocktailrecipesapp

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

//TODO import

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun register_checkAPI_call() {
        val call = serverAPI.register("username", "password_hash");
        Assert.assertEquals("{ success: 0 }", call);
    }

    fun login_checkAPI_call() {
        val call = serverAPI.login("username", "password_hash");
        Assert.assertEquals(call, "{ user_id: 0 }");
    }

    fun addRecipe_checkAPI_call() {
        val call = serverAPI.addRecipe("Freddy Krueger", 5, 1, "Fill in all spirits and mix up.", listOf("STROH 80","Sambuca","Jägermeister"));
        Assert.assertEquals(call, "{ success: 0 }");
    }

    fun rateRecipe_checkAPI_call() {
        val call = serverAPI.rateRecipe(1, 1, 1);
        Assert.assertEquals(call, "{ success: 0 }");
    }

    fun getRecipes_checkAPI_call() {
        val call = serverAPI.getRecipes();
        Assert.assertEquals(call, "{ success: 0 }");
    }

    fun changePassword_checkAPI_call() {
        val call = serverAPI.changePassword("password_hash_old", "password_hash_new");
        Assert.assertEquals(call, "{ success: 0 }");
    }

    fun deleteRecipe_checkAPI_call() {
        val call = serverAPI.deleteRecipe(1, 1);
        Assert.assertEquals(call, "{ success: 0 }");
    }

    fun likeRecipe_checkAPI_call() {
        val call = serverAPI.likeRecipe(1, 1);
        Assert.assertEquals(call, "{ success: 0 }");
    }
}