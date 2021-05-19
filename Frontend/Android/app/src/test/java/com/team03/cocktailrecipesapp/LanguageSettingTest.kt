package com.team03.cocktailrecipesapp

import org.junit.Assert
import org.junit.Test

class LanguageSettingTest {

    var userProfile = UserSettingsActivity()
    @Test
    fun callChangeLanguage()
    {

//        val ret = userProfile.setLanguage()
//        Assert.assertTrue("Changing of language didn't work as expected", ret)
    }

    @Test
    fun switchUpdateFields(){
        val ret = userProfile.updateFields()
        Assert.assertTrue("Updating fields didn't work as expected", ret)
    }

}