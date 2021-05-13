package com.team03.cocktailrecipesapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.Response
import org.hamcrest.core.IsNot.not
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
        onView(withId(R.id.btnLogin)).perform(click())
    }


    fun PerformLogout() {
        // user-profile
        onView(withId(R.id.imgBtnAvatar)).perform(click())

        // logout
        onView(withId(R.id.logoutButton)).check(matches(withText(R.string.logout_button_text)))
        onView(withId(R.id.logoutButton)).perform(click())

        // check if we are in login page
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

    fun PerformLogin(username: String, password: String)
    {
        if (userId > 0)
            PerformLogout()

        onView(withId(R.id.etUsername)).perform(typeText(username), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText(password), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())

    }

    fun PerformLoginAsNonAdmin()
    {
        PerformLogin("osama", "1234qwer")
    }

    fun PerformLoginAsAdmin()
    {
        PerformLogin("daniel", "1234qwer")
    }

    @Test
    fun PerformLogoutTest()
    {
        PerformLogout()
    }

    @Test
    fun ButtonDeleteRecipeAdminIsVisible()
    {
        PerformLoginAsAdmin()
        Thread.sleep(2000)
        onView(withText("testi")).perform(scrollTo())
        onView(withText("testi")).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.delete_recipe_button)).check(matches(isDisplayed()))
    }

    @Test
    fun ButtonDeleteRecipeIsNotVisible()
    {
        PerformLoginAsNonAdmin()
        Thread.sleep(2000)
        onView(withText("testi")).perform(scrollTo())
        onView(withText("testi")).perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.delete_recipe_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun VisitUserProfileTest() {
        PerformLoginAsNonAdmin()
        Thread.sleep(2000)
        onView(withText("testi")).perform(scrollTo())
        onView(withText("testi")).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.cocktail_creator_username)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.user_profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.user_profile_username)).check(matches(isDisplayed()))
    }
}