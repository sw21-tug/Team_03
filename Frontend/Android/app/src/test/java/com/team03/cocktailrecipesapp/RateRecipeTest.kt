package com.team03.cocktailrecipesapp;

import org.junit.Assert.*
import org.junit.Test

class RateRecipeTest
{
    val detailActivity : CocktailDetailActivity = CocktailDetailActivity()

    @Test
    fun perfromRateTest() {
        val completed_rating: Boolean = detailActivity.sendRating()
        assertTrue(completed_rating)
    }

}
