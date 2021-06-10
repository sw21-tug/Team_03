package com.team03.cocktailrecipesapp

import android.app.Activity
import android.view.View
import androidx.annotation.Nullable
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.core.internal.deps.guava.base.Predicate
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.TreeIterables
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import java.time.LocalDateTime

val testListener = TestListener()
val testErrorListener = TestErrorListener()

object ViewCounter {
    fun getCount(viewMatcher: Matcher<View>, countLimit: Int = 9999): Int {
        var actualViewsCount = 0
        do {
            try {
                Espresso.onView(ViewMatchers.isRoot())
                    .check(ViewAssertions.matches(withViewCount(viewMatcher, actualViewsCount)))
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
    TestRecipe("Test recipe 1", "Test description 1", listOf(
        TestIngredient("Cola", 100, "ml"),
        TestIngredient("Vodka", 30, "ml"),
        TestIngredient("Ice", 5, "pcs")))
val test_recipe_2: TestRecipe =
    TestRecipe("Test recipe 2", "Test description 2", listOf(
        TestIngredient("Tequila", 50, "ml"),
        TestIngredient("Vodka", 30, "ml"),
        TestIngredient("Lime-juice", 5, "g"),
        TestIngredient("Ice", 3, "pcs")))
val test_recipe_3: TestRecipe =
    TestRecipe("Test recipe 3", "Test description 3", listOf(
        TestIngredient("Cola", 100, "ml"),
        TestIngredient("Rum", 30, "ml"),
        TestIngredient("Ice", 3, "pcs")))
val test_recipe_4: TestRecipe =
    TestRecipe("Test recipe delete", "Test description delete", listOf(
        TestIngredient("Tequila", 100, "ml"),
        TestIngredient("Rum", 30, "ml"),
        TestIngredient("Ice", 3, "pcs")))
val test_recipes: List<TestRecipe> = listOf(test_recipe_1, test_recipe_2, test_recipe_3, test_recipe_4)
var test_recipes_index: Int = 0


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
    InstrumentationRegistry.getInstrumentation()
        .runOnMainSync { run { currentActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
            Stage.RESUMED).elementAtOrNull(0) } }

    if (currentActivity?.localClassName == "LoginActivity")
    {
        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .perform(ViewActions.typeText("daniel"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
            .perform(ViewActions.typeText("1234qwer"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click())
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

fun checkLoggedOut() {
    if (userId != 0)
        PerformLogout()
}

private fun PerformLogout() {
    // user-profile
    Espresso.onView(ViewMatchers.withId(R.id.imgBtnAvatar)).perform(ViewActions.click())

    Thread.sleep(500)
    // logout
    Espresso.onView(ViewMatchers.withId(R.id.logoutButton))
        .check(ViewAssertions.matches(ViewMatchers.withText(R.string.logout_button_text)))
    Espresso.onView(ViewMatchers.withId(R.id.logoutButton)).perform(ViewActions.click())

    Thread.sleep(1000)

    // check if we are in login page
    Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

private fun PerformLogin(username: String, password: String)
{
    if (userId > 0)
        PerformLogout()

    Espresso.onView(ViewMatchers.withId(R.id.etUsername))
        .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard())
    Espresso.onView(ViewMatchers.withId(R.id.etPassword))
        .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())
    Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click())
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
