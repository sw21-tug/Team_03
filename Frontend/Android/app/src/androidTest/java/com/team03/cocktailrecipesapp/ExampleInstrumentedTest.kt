package com.team03.cocktailrecipesapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*



import com.team03.cocktailrecipesapp.ui.login.LoginViewModel
import com.team03.cocktailrecipesapp.ui.login.LoginActivity



//"../../com.team03.cocktailrecipesapp/ui.login/LoginViewModel"

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }

    fun useAppContext()
    {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.team03.cocktailrecipesapp", appContext.packageName)


        val password:String = "12345678";
        val valid = isPasswordValid(password)
        assertEquals(true, LoginViewModel::isPasswordValid(password))

    fun password()
    {
        (LoginViewModel(loginRepository = password))(password)


       // onView(withId(R.id.password)).perform(click())
    }



    }




}