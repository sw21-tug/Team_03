package com.team03.cocktailrecipesapp

import org.junit.Assert
import org.junit.Test

class LanguageSettingTest {

    @Test
    fun callChangeLanguage()
    {
        var userProfile = UserProfile()
        val ret = userProfile.changeLanguage()

        Assert.assertTrue("Changing of language didn't work as expected", ret)
    }

}