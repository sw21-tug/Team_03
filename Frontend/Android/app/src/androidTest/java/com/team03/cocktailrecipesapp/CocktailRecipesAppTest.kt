package com.team03.cocktailrecipesapp

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.Nullable
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.base.Predicate
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.android.volley.Response
import org.hamcrest.core.IsNot.not
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
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

    object ViewCounter {
        fun getCount(viewMatcher: Matcher<View>, countLimit: Int = 9999): Int {
            var actualViewsCount = 0
            do {
                try {
                    onView(isRoot())
                        .check(matches(withViewCount(viewMatcher, actualViewsCount)))
                    return actualViewsCount
                } catch (ignored: Error) {
                }
                actualViewsCount++
            } while (actualViewsCount < countLimit)
            throw Exception("Counting $viewMatcher was failed. Count limit exceeded")
        }

        fun withViewCount(viewMatcher: Matcher<View>, expectedCount: Int): Matcher<View?>? {
            return object : TypeSafeMatcher<View?>() {
                var actualCount = -1
                override fun describeTo(description: Description) {
                    if (actualCount >= 0) {
                        description.appendText("With expected number of items: $expectedCount")
                        description.appendText("\n With matcher: ")
                        viewMatcher.describeTo(description)
                        description.appendText("\n But got: $actualCount")
                    }
                }

                override fun matchesSafely(root: View?): Boolean {
                    actualCount = 0
                    val iterable = TreeIterables.breadthFirstViewTraversal(root)
                    actualCount = Iterables.filter(iterable, withMatcherPredicate(viewMatcher)).count()
                    return actualCount == expectedCount
                }
            }
        }

        private fun withMatcherPredicate(matcher: Matcher<View>): Predicate<View?>? {
            return object : Predicate<View?> {
                override fun apply(@Nullable view: View?): Boolean {
                    return matcher.matches(view)
                }
            }
        }
    }

    // Data class for test recipe + ingredients
    data class TestIngredient(
        var name: String,
        var amount: Int,
        var unit: String
    )

    data class TestRecipe(
        var name: String,
        var description: String,
        var ingredients: List<TestIngredient>
    )
    // Dummy test recipes for testing
    val test_recipe_1: TestRecipe =
        TestRecipe("Vodka Makava", "Mix Vodka and makava. Add ice on own preferences.", listOf(
            TestIngredient("Makava", 100, "ml"),
            TestIngredient("Vodka", 30, "ml"),
            TestIngredient("Ice", 5, "pcs")))

    val test_recipe_2: TestRecipe =
        TestRecipe("Cola Rum", "mixing cola with Rum", listOf(
            TestIngredient("Cola", 200, "ml"),
            TestIngredient("Rum", 30, "ml"),
            TestIngredient("Ice", 3, "pcs")))

    val test_recipe_3: TestRecipe =
        TestRecipe("Pina Colada", "    For a Pina Colada, first cut the pineapple slice into cubes and put it in the blender.\n" +
                "    Add pineapple juice, Cream of Coconut (or coconut syrup), rum, cream (this will make the cocktail even creamier) lemon drops and put everything in the blender and mix well for about 25 seconds.\n" +
                "    Pour the finished Pina Colada into a balloon glass (fancy glass), fill with crushed ice and garnish with a fresh pineapple slice and a cocktail cherry.\n", listOf(
            TestIngredient("Cream of Coconut", 4, "cl"),
            TestIngredient("Whipped cream", 2, "cl"),
            TestIngredient("Pineapple juice", 8, "cl"),
            TestIngredient("Lime juice", 1, "cl"),
            TestIngredient("Rum", 6, "cl")))

    val test_recipe_4: TestRecipe =
        TestRecipe("Averna Sour", "Mix Averna with Lemon", listOf(
            TestIngredient("Averna", 6, "cl"),
            TestIngredient("Lime-juice", 2, "cl"),
            TestIngredient("Ice", 2, "pcs")))

    val test_recipe_5: TestRecipe =
        TestRecipe("Mojito", "    STEP 1\n" +
                "\n" +
                "    Muddle the lime juice, sugar and mint leaves in a small jug, crushing the mint as you go – you can use the end of a rolling pin for this. Pour into a tall glass and add a handful of ice.\n" +
                "    STEP 2\n" +
                "\n" +
                "    Pour over the rum, stirring with a long-handled spoon. Top up with soda water, garnish with mint and serve.", listOf(
            TestIngredient("White Rum", 60, "ml"),
            TestIngredient("Lime-juice", 1, "cl"),
            TestIngredient("Mint", 5, "pcs"),
            TestIngredient("Soda water", 2, "cl"),
            TestIngredient("Brown sugar", 1, "tsp")))


    val test_recipes: List<TestRecipe> = listOf(test_recipe_1, test_recipe_2, test_recipe_3, test_recipe_4, test_recipe_5)
    var test_recipes_index: Int = 0

    @Test
    fun AddIngredients() {
        login()
        // Get recipe for testing
        if (test_recipes_index > 2) { test_recipes_index = 0 }
        val recipe: TestRecipe = test_recipes.get(test_recipes_index)

        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(typeText(recipe.name), closeSoftKeyboard())
        onView(withId(R.id.etRecipeDescription)).perform(typeText(recipe.description), closeSoftKeyboard())
        onView(withId(R.id.btnManageIngredients)).perform(click())
        Thread.sleep(500)

        // Get ingredients of recipe
        for (ingredient in recipe.ingredients)
        {
            onView(withText(ingredient.name)).check(matches(isNotChecked())).perform(
                scrollTo(), click())
            onView(allOf(
                withId(R.id.etIngredientAmount), withParent(allOf(
                    withId(R.id.etIngredientLayout), hasSibling(withText(ingredient.name)))))).perform(
                scrollTo(), typeText(""+ingredient.amount))
            onView(allOf(
                withId(R.id.etIngredientUnit), withParent(allOf(
                    withId(R.id.etIngredientLayout), hasSibling(withText(ingredient.name)))))).perform(
                scrollTo(), typeText(ingredient.unit))
            Thread.sleep(500)
        }
        Thread.sleep(500)

        onView(withId(R.id.btnConfirmIngredients)).perform(click())
        onView(withId(R.id.difficulty_picker)).perform(swipeDown())
        onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())
        onView(withId(R.id.btnAddRecipe)).perform(click())
    }

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
            onView(withId(R.id.btnLogin)).perform(click())
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
            "",
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
        // login

//        onView(withId(R.id.etUsername)).perform(typeText("daniel"), closeSoftKeyboard())
//        onView(withId(R.id.etPassword)).perform(typeText("1234qwer"), closeSoftKeyboard())
//        onView(withId(R.id.btnLogin)).perform(click())

        // user-profile
        onView(withId(R.id.imgBtnAvatar)).perform(click())

        Thread.sleep(500)
        // logout
        onView(withId(R.id.logoutButton)).check(matches(withText(R.string.logout_button_text)))
        onView(withId(R.id.logoutButton)).perform(click())

        Thread.sleep(1000)

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
        Thread.sleep(2000)
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
    fun AddNewRecipe() {
        login()
        // Get recipe for testing
        if (test_recipes_index > 3) { test_recipes_index = 0 }
        val recipe: TestRecipe = test_recipes.get(test_recipes_index)

        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(typeText(recipe.name), closeSoftKeyboard())
        onView(withId(R.id.etRecipeDescription)).perform(typeText(recipe.description), closeSoftKeyboard())
        onView(withId(R.id.btnManageIngredients)).perform(click())
        Thread.sleep(500)

        // Get ingredients of recipe
        for (ingredient in recipe.ingredients)
        {
            onView(withText(ingredient.name)).check(matches(isNotChecked())).perform(
                scrollTo(), click())
            onView(allOf(
                withId(R.id.etIngredientAmount), withParent(allOf(
                    withId(R.id.etIngredientLayout), hasSibling(withText(ingredient.name)))))).perform(
                        scrollTo(), typeText(""+ingredient.amount))
            onView(allOf(
                withId(R.id.etIngredientUnit), withParent(allOf(
                    withId(R.id.etIngredientLayout), hasSibling(withText(ingredient.name)))))).perform(
                        scrollTo(), typeText(ingredient.unit))
            Thread.sleep(500)
        }
        Thread.sleep(500)

        onView(withId(R.id.btnConfirmIngredients)).perform(click())
        onView(withId(R.id.difficulty_picker)).perform(swipeDown())
        onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())
        onView(withId(R.id.btnAddRecipe)).perform(click())
    }

    @Test
    fun AddNewRecipes() {
        for (recipeIndex in 0..2)
        {
            test_recipes_index = recipeIndex
            AddNewRecipe()
            Thread.sleep(1000)
        }
    }

    @Test
    fun DeleteRecipe()  {
        test_recipes_index = 3
        AddNewRecipe()
        Thread.sleep(1000)
        onView(withId(R.id.txtViewSeeAll)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.searchRecipesCardView)).perform(click())
        onView(withId(R.id.searchRecipesView)).perform(typeText(test_recipes[3].name), closeSoftKeyboard())
        Thread.sleep(1000)
        onView(allOf(
            withId(R.id.trendingCocktailCard), withParentIndex(0))).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.delete_recipe_button)).perform(click())
        Thread.sleep(1000)
        onView(withText("YES")).perform(click())
    }

    @Test
    fun VisitRecommendedCocktailDetailTest() {
        PerformLoginAsNonAdmin()
        val count = ViewCounter.getCount(withId(R.id.recommendedCocktailCard))

        for (i in 0 until count)
        {
            onView(allOf(
                withId(R.id.recommendedCocktailCard), withParentIndex(i)))
                .perform(scrollTo(), click())
            Thread.sleep(1000)
            onView(isRoot()).perform(pressBack())
            Thread.sleep(2000)
        }
    }

    @Test
    fun VisitTrendingCocktailDetailTest() {
        PerformLoginAsNonAdmin()
        val count = ViewCounter.getCount(withId(R.id.trendingCocktailCard))

        for (i in 0 until count)
        {
            onView(allOf(
                withId(R.id.trendingCocktailCardImageView), withParent(allOf(
                    withId(R.id.trendingCocktailListCardLinearLayout), withParent(allOf(
                        withId(R.id.trendingCocktailCard), withParentIndex(i)))))))
                .perform(scrollTo(), click())
            Thread.sleep(1000)
            onView(isRoot()).perform(pressBack())
            Thread.sleep(2000)
        }
    }

    fun filterByName()
    {
        onView(withId(R.id.txtViewSeeAll)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.searchRecipesView)).perform(typeText("testi"), closeSoftKeyboard())
        Thread.sleep(500)
        onView(allOf(withId(R.id.cocktail_name), withText("testi"))).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.cocktail_name)).check(matches(withText("testi")))
    }

    @Test
    fun filterByNameNotFound()
    {
        onView(withId(R.id.txtViewSeeAll)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.searchRecipesView)).perform(typeText("testi"), closeSoftKeyboard())
        Thread.sleep(500)
        onView(allOf(withId(R.id.cocktail_name), not(withText("test"))))
    }

    @Test
    fun filterByNameWithNewRecipe()
    {
        AddNewRecipe()
        Thread.sleep(1000)
        onView(withId(R.id.txtViewSeeAll)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.searchRecipesView)).perform(typeText("Test recipe 1"), closeSoftKeyboard())
        Thread.sleep(500)
        onView(allOf(withId(R.id.cocktail_name), withText("Test recipe 1"))).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.cocktail_name)).check(matches(withText("Test recipe 1")))
    }

    @Test
    fun initial_fillDbWithCleanUser()
    {
        Thread.sleep(500)
        var currentActivity: Activity? = null
        getInstrumentation().runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).elementAtOrNull(0) } }
        Thread.sleep(500)

        if (currentActivity?.localClassName?.contains("LoginActivity") == false)
        {
            PerformLogout()
        }
        Thread.sleep(500)

        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("Daniel"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("1234qwer"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("1234qwer"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("Sepp"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("1234Sepp"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("1234Sepp"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("Sabine"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("1234Sabine"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("1234Sabine"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("Heidi"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("1234Heidi"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("1234Heidi"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("Herbet"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("1234Herbert"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("1234Herbert"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("Michaela"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("1234Michaela"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("1234Michaela"), closeSoftKeyboard())
        onView(withId(R.id.btn_register)).perform(click())
        Thread.sleep(2000)

    }

    @Test
    fun initial_fillDbWithCleanRecipes()
    {
        var i: Int = 2
        login()
        for (recipe in test_recipes) {
            i++
            onView(withId(R.id.add_recipe_button)).perform(click())
            Thread.sleep(500)
            onView(withId(R.id.etRecipeName)).perform(replaceText(recipe.name))
            onView(withId(R.id.etRecipeDescription)).perform(replaceText(recipe.description))
            onView(withId(R.id.btnManageIngredients)).perform(click())
            Thread.sleep(500)

            // Get ingredients of recipe
            for (ingredient in recipe.ingredients) {
                onView(withText(ingredient.name)).check(matches(isNotChecked())).perform(
                    scrollTo(), click()
                )
                onView(
                    allOf(
                        withId(R.id.etIngredientAmount), withParent(
                            allOf(
                                withId(R.id.etIngredientLayout),
                                hasSibling(withText(ingredient.name))
                            )
                        )
                    )
                ).perform(
                    scrollTo(), replaceText("" + ingredient.amount)
                )
                onView(
                    allOf(
                        withId(R.id.etIngredientUnit), withParent(
                            allOf(
                                withId(R.id.etIngredientLayout),
                                hasSibling(withText(ingredient.name))
                            )
                        )
                    )
                ).perform(
                    scrollTo(), replaceText(ingredient.unit)
                )
                Thread.sleep(500)
            }

            onView(withId(R.id.btnConfirmIngredients)).perform(click())
            Thread.sleep(500)

            for (x in 0..i) {
                onView(withId(R.id.difficulty_picker)).perform(swipeDown())
                onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())
            }

            onView(withId(R.id.btnAddRecipe)).perform(click())
            Thread.sleep(1000)
        }
    }

    @Test
    fun initial_fillDbWithCleanIngredients() {

        val ingredients: MutableList<String> = mutableListOf(
            "Makava", "Vodka", "Ice", "Cola", "Rum",
            "Cream of Coconut", "Whipped cream",
            "Pineapple juice", "Lime juice",
            "Averna", "Mint", "Soda water",
            "Brown sugar", "White Rum"
        )


        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(replaceText("Kingscup"))
        onView(withId(R.id.etRecipeDescription)).perform(replaceText("Mix all drinks you have"))
        onView(withId(R.id.btnManageIngredients)).perform(click())
        for (ingredient in ingredients) {
            onView(withId(R.id.btnAddIngredient)).perform(click())
            Thread.sleep(300)
            onView(withId(R.id.etRecipeName)).perform(replaceText(ingredient))
            onView(withId(R.id.btnSaveIngrediant)).perform(click())
            onView(
                allOf(
                    withId(R.id.etIngredientAmount), withParent(
                        allOf(
                            withId(R.id.etIngredientLayout), hasSibling(withText(ingredient))
                        )
                    )
                )
            ).perform(
                scrollTo(), replaceText("5")
            )
            onView(
                allOf(
                    withId(R.id.etIngredientUnit), withParent(
                        allOf(
                            withId(R.id.etIngredientLayout), hasSibling(withText(ingredient))
                        )
                    )
                )
            ).perform(
                scrollTo(), replaceText("cl")
            )
            Thread.sleep(300)
        }
        onView(withId(R.id.btnConfirmIngredients)).perform(click())
        Thread.sleep(200)
        onView(withId(R.id.btnAddRecipe)).perform(click())
    }
    fun filterByIngredientWithNewRecipe()
    {
        login()
        // Get recipe for testing
        test_recipes_index = 3;
        val recipe: TestRecipe = test_recipes.get(test_recipes_index)

        onView(withId(R.id.add_recipe_button)).perform(click())
        onView(withId(R.id.etRecipeName)).perform(typeText(recipe.name), closeSoftKeyboard())
        onView(withId(R.id.etRecipeDescription)).perform(typeText(recipe.description), closeSoftKeyboard())
        onView(withId(R.id.btnManageIngredients)).perform(click())
        Thread.sleep(500)

        // Get ingredients of recipe
        for (ingredient in recipe.ingredients)
        {
            onView(withText(ingredient.name)).check(matches(isNotChecked())).perform(
                scrollTo(), click())
            onView(allOf(
                withId(R.id.etIngredientAmount), withParent(allOf(
                    withId(R.id.etIngredientLayout), hasSibling(withText(ingredient.name)))))).perform(
                scrollTo(), typeText(""+ingredient.amount))
            onView(allOf(
                withId(R.id.etIngredientUnit), withParent(allOf(
                    withId(R.id.etIngredientLayout), hasSibling(withText(ingredient.name)))))).perform(
                scrollTo(), typeText(ingredient.unit))
            Thread.sleep(500)
        }
        Thread.sleep(500)

        onView(withId(R.id.btnConfirmIngredients)).perform(click())
        onView(withId(R.id.difficulty_picker)).perform(swipeDown())
        onView(withId(R.id.timer_picker_minutes)).perform(swipeDown())
        onView(withId(R.id.btnAddRecipe)).perform(click())

        Thread.sleep(500)
        onView(withId(R.id.txtViewSeeAll)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.btnFilterByIngredients)).perform(click())
        Thread.sleep(500)
        onView(withText("Averna")).check(matches(isNotChecked())).perform(
            scrollTo(), click())
        onView(withId(R.id.btnConfirmIngredients)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.searchRecipesView)).perform(typeText("Filtered Recipe"), closeSoftKeyboard())
        Thread.sleep(500)
        onView(allOf(withId(R.id.cocktail_name), withText("Filtered Recipe"))).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.cocktail_name)).check(matches(withText("Filtered Recipe")))
    }
}