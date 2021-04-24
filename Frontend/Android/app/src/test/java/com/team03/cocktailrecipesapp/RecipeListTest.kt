package com.team03.cocktailrecipesapp

import org.junit.Assert.*
import org.junit.Test

class RecipeListTest
{
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
        }
    }
}