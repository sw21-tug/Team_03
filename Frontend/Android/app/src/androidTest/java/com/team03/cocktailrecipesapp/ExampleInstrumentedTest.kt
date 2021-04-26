package com.team03.cocktailrecipesapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.Response
import org.json.JSONObject
import org.junit.Assert
import org.junit.Rule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val server = serverAPI(context);
    private val testListener = TestListener();
    private val testErrorListener = TestErrorListener();

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

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
        var creator_username: String? = null,
        var ratin: Float
    )

    data class AnswerRecipes(
        var recipes: List<Recipe>
    )

    @Test
    fun register_checkAPI_calls() {
        val answer = server.register("ahmed",
            "4aeb2000b9de5858f5e5e0b7eda52f253caf19582c67cbbb453be6987ecc1baf27d75670e39f78058fb1ebee3d16b83d1cbdc8d3628636377b2458ea5bf12ff2",
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);

        Assert.assertEquals(0, answer)
    }

    fun login_checkAPI_call() {
        val answer = server.login("username",
            "password_hash",
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);

        Assert.assertEquals(0, answer)
    }

    fun getRecipes_checkAPI_call() {
        val answer = server.getRecipes(testListener as Response.Listener<JSONObject>, testErrorListener as Response.ErrorListener);

        Assert.assertEquals(0, answer)

        /*for (recipe in answer.recipes)
        {
            Assert.assertNotNull(recipe.creation_time);
            Assert.assertNotNull(recipe.creator_username);
            Assert.assertNotNull(recipe.instruction);
            Assert.assertNotNull(recipe.name);

            Assert.assertTrue(recipe.difficulty > 0)
            Assert.assertTrue(recipe.difficulty <= 10)
        }*/
    }

    fun changePassword_checkAPI_call() {
        val answer = server.changePassword(-1,
            "old_password_hash",
            "new_password_hash",
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        Assert.assertEquals(0, answer)
    }

    fun deleteRecipe_checkAPI_call() {
        val answer = server.deleteRecipe(-1,
            -1,
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        Assert.assertEquals(0, answer)
    }

    fun likeRecipe_checkAPI_call() {
        val answer = server.likeRecipe(-1,
            -1,
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        Assert.assertEquals(0, answer)
    }

    fun addRecipe_checkAPI_call() {
        val answer = server.addRecipe(1,
            "Freddy Krueger",
            5,
            1,
            "First put in Stroh80 in 1 2cl shot glass. Take a teaspoon and " +
                    "layer the Jaeger onto the rum. Then finally do the same with the " +
                    "Baileys. Enjoy!",
            listOf("Stroh80", "Jaegermeister", "Baileys"),
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);

        Assert.assertEquals(0, answer)
    }

    fun rateRecipe_checkAPI_call() {
        val answer = server.likeRecipe(-1,
            -1,
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        Assert.assertEquals(0, answer)
    }

    @Test
    fun loginDataTest(){
        onView(withId(R.id.btnSwitchToLogin)).perform(click())
        onView(withId(R.id.etUsername)).perform(typeText("Testusername"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("Testpassword"), closeSoftKeyboard())
    }

    @Test
    fun loginButton() {
        onView(withId(R.id.btnSwitchToLogin)).perform(click())
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun registerButton() {
        onView(withId(R.id.btnSwitchToLogin)).perform(click())
        onView(withId(R.id.btnRegister)).perform(click())
    }

    @Test
    fun fillOutRegistrationform() {
        onView(withId(R.id.btnSwitchToLogin)).perform(click())
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("John Doe"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("password1"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("password1"), closeSoftKeyboard())
    }

    @Test
    fun fillOutRegistrationform_ErrorPasswordRepeat() {
        Assert.assertEquals(RegisterFuncs().validateInput("John Doe", "password1", "password2"), -4);
    }

    @Test
    fun fillOutRegistrationform_ErrorEmptyPassword() {
        Assert.assertEquals(RegisterFuncs().validateInput("John Doe", "", "password2"), -2);
    }

    @Test
    fun fillOutRegistrationform_ErrorEmptyPasswordRepeat() {
        Assert.assertEquals(RegisterFuncs().validateInput("John Doe", "password1", ""), -3);
    }
}