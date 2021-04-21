package com.team03.cocktailrecipesapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.team03.cocktailrecipesapp", appContext.packageName)
    }

    @Test
    fun getRecipes() {
        val recipe_1 = Recipe("cocktail 1")
        val recipe_2 = Recipe("cocktail 2")
        val recipe_3 = Recipe("cocktail 3")
        val recipe_list = listOf(recipe_1, recipe_2, recipe_3)

        recipe_list.forEach {
            assertNotNull("recipe: name <str> invalid!", it.name.length)
            assertTrue("recipe: preptime_minutes <int> invalid!", (it.preptime_minutes > 0))
            assertTrue("recipe: difficulty <int> invalid!", (it.difficulty > 0))
            assertNotNull("recipe: instruction <str> invalid!", it.instruction.length)
            assertNotNull("recipe: creation_time <str> invalid!", it.creation_time.length)
            assertNotNull("recipe: creator_username <str> invalid!", it.creator_username.length)
            // valid recipe, create card and add to list
            assertTrue(addRecipeToList(it))
        }
    }

    fun addRecipeToList(recipe: Recipe) : Boolean {
        // TODO: create card with cocktail and inflate recipe list
        return false
    }
}