package com.team03.cocktailrecipesapp

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.android.volley.Response
import org.hamcrest.CoreMatchers.*
import org.json.JSONObject
import org.junit.Assert
import org.junit.Rule
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

    fun login() {
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }

        if (currentActivity?.localClassName == "LoginActivity")
        {
            onView(withId(R.id.etUsername)).perform(typeText("daniel"), closeSoftKeyboard())
            onView(withId(R.id.etPassword)).perform(typeText("1234qwer"), closeSoftKeyboard())
            onView(withId(R.id.btnBack)).perform(click())
        }
        else
        {
        }
    }

    fun util_waitResult() {
        while (!testErrorListener.gotResponse && !testListener.gotResponse);

        Assert.assertEquals(true, testListener.gotResponse);
        Assert.assertEquals(false, testErrorListener.gotResponse);
    }

    @Test
    fun register_checkAPI_calls() {
        val answer = server.register("daniel",
            "4aeb2000b9de5858f5e5e0b7eda52f253caf19582c67cbbb453be6987ecc1baf27d75670e39f78058fb1ebee3d16b83d1cbdc8d3628636377b2458ea5bf12ff2",
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);

        util_waitResult();
    }

    @Test
    fun login_checkAPI_call() {
        val answer = server.login("username",
            "password_hash",
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);

        util_waitResult();
    }

    @Test
    fun getRecipes_checkAPI_call() {
        val answer = server.getRecipes(testListener as Response.Listener<JSONObject>, testErrorListener as Response.ErrorListener);
        util_waitResult();
    }

    @Test
    fun changePassword_checkAPI_call() {
        val answer = server.changePassword(-1,
            "old_password_hash",
            "new_password_hash",
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        util_waitResult();
    }

    @Test
    fun deleteRecipe_checkAPI_call() {
        val answer = server.deleteRecipe(-1,
            -1,
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        util_waitResult();
    }

    @Test
    fun likeRecipe_checkAPI_call() {
        val answer = server.likeRecipe(-1,
            -1,
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        util_waitResult();
    }

    @Test
    fun addRecipe_checkAPI_call() {
        val answer = server.addRecipe(1,
            "Freddy Krueger",
            5,
            1,
            "First put in Stroh80 in 1 2cl shot glass. Take a teaspoon and " +
                    "layer the Jaeger onto the rum. Then finally do the same with the " +
                    "Baileys. Enjoy!",
            listOf("Stroh80", "Jaegermeister", "Baileys"),
            listOf(120, 200, 20),
            listOf("ml", "ml", "ml"),
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);

        util_waitResult();
    }

    @Test
    fun rateRecipe_checkAPI_call() {
        val answer = server.likeRecipe(-1,
            -1,
            testListener as Response.Listener<JSONObject>,
            testErrorListener as Response.ErrorListener);
        util_waitResult();
    }

    @Test
    fun loginDataTest(){
        onView(withId(R.id.etUsername)).perform(typeText("Testusername"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("Testpassword"), closeSoftKeyboard())
    }


    @Test
    fun fillOutRegistrationform() {
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
    fun fillOutRegistrationform_ErrorEmptyUsername() {
        Assert.assertEquals(RegisterFuncs().validateInput("", "password1", "password2"), -1);
    }

    @Test
    fun fillOutRegistrationform_ErrorEmptyPassword() {
        Assert.assertEquals(RegisterFuncs().validateInput("John Doe", "", "password2"), -2);
    }

    @Test
    fun fillOutRegistrationform_ErrorEmptyPasswordRepeat() {
        Assert.assertEquals(RegisterFuncs().validateInput("John Doe", "password1", ""), -3);
    }

    @Test
    fun RegisteranLoginWithCorrectInput() {
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("UsernameTest123"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())

        Thread.sleep(2000)

        onView(withId(R.id.etUsername)).perform(typeText("UsernameTest123"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.btnBack)).perform(click())
    }

    @Test
    fun PerformLogout() {
        // login

        //onView(withId(R.id.etUsername)).perform(typeText("daniel"), closeSoftKeyboard())
        //onView(withId(R.id.etPassword)).perform(typeText("1234qwer"), closeSoftKeyboard())
        //onView(withId(R.id.btnLogin)).perform(click())

        // user-profile
        onView(withId(R.id.imgBtnAvatar)).perform(click())

        // logout
        onView(withId(R.id.logoutButton)).check(matches(withText(R.string.logout_button_text)))
        onView(withId(R.id.logoutButton)).perform(click())

        // check if we are in login page
        onView(withId(R.id.btnBack)).check(matches(isDisplayed()))
    }
    @Test
    fun AddNewRecipeWithNoIngredients() {
        login()
        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(typeText("Recipe Without Ingredients"), closeSoftKeyboard())
        onView(withId(R.id.etRecipeDescription)).perform(typeText("This Recipe has no Ingredients."), closeSoftKeyboard())

        onView(withId(R.id.btnManageIngredients)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.btnConfirmIngredients)).perform(click())

        onView(withId(R.id.difficulty_picker)).perform(swipeDown())
        onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())
        onView(withId(R.id.btnAddRecipe)).perform(click())
    }

    @Test
    fun AddNewRecipeTryWithoutIngredientsAddIngredientsAfter() {
        login()
        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(typeText("Recipe Without Ingredients"), closeSoftKeyboard())
        onView(withId(R.id.btnManageIngredients)).perform(click())
        Thread.sleep(1000)

        onView(withText("Vodka")).check(matches(isNotChecked())).perform(click())
        onView(withText("Cola")).check(matches(isNotChecked())).perform(click())

        onView(withId(R.id.btnConfirmIngredients)).perform(click())

        onView(withId(R.id.etRecipeDescription)).perform(typeText("This Recipe has no Ingredients."), closeSoftKeyboard())

        onView(withId(R.id.difficulty_picker)).perform(swipeDown())
        onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())

        onView(withId(R.id.btnAddRecipe)).perform(click())
    }

    @Test
    fun AddNewRecipeTryAddingIngredientsMultipleTime() {
        login()
        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(typeText("Recipe Without Ingredients"), closeSoftKeyboard())

        for (i in 0..5)
        {
            onView(withId(R.id.btnManageIngredients)).perform(click())
            Thread.sleep(1000)

            onView(withText("Vodka")).check(matches(isNotChecked())).perform(click())
            onView(withText("Cola")).check(matches(isNotChecked())).perform(click())
            onView(withText("Beer")).check(matches(isNotChecked())).perform(click())

            onView(withId(R.id.btnConfirmIngredients)).perform(click())
        }


        onView(withId(R.id.etRecipeDescription)).perform(typeText("This Recipe has no Ingredients."), closeSoftKeyboard())

        onView(withId(R.id.difficulty_picker)).perform(swipeDown())
        onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())

        onView(withId(R.id.btnAddRecipe)).perform(click())
    }

    @Test
    fun AddNewRecipe() {
        login();
        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(typeText("Recipe Without Ingredients"), closeSoftKeyboard())
        onView(withId(R.id.etRecipeDescription)).perform(typeText("This Recipe has no Ingredients."), closeSoftKeyboard())

        onView(withId(R.id.btnManageIngredients)).perform(click())
        Thread.sleep(500)
        onView(withText("Vodka")).check(matches(isNotChecked())).perform(click())
        onView(withText("Cola")).check(matches(isNotChecked())).perform(click())
        onView(withText("Beer")).check(matches(isNotChecked())).perform(click())
        onView(withId(R.id.btnConfirmIngredients)).perform(click())

        onView(withId(R.id.difficulty_picker)).perform(swipeDown())
        onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())
        onView(withId(R.id.btnAddRecipe)).perform(click())

    }

    @Test
    fun AddNewRecipes() {
        login();
        for (i in 0..3)
        {
            onView(withId(R.id.add_recipe_button)).perform(click())
            onView(withId(R.id.etRecipeName)).perform(typeText("Recipe Without Ingredients"), closeSoftKeyboard())
            onView(withId(R.id.etRecipeDescription)).perform(typeText("This Recipe has no Ingredients."), closeSoftKeyboard())

            onView(withId(R.id.btnManageIngredients)).perform(click())
            Thread.sleep(500)
            onView(withText("Vodka")).check(matches(isNotChecked())).perform(click())
            onView(withText("Cola")).check(matches(isNotChecked())).perform(click())
            onView(withText("Beer")).check(matches(isNotChecked())).perform(click())
            onView(withId(R.id.btnConfirmIngredients)).perform(click())

            onView(withId(R.id.difficulty_picker)).perform(swipeDown())
            onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())
            onView(withId(R.id.btnAddRecipe)).perform(click())
            Thread.sleep(1000)
        }
    }

}