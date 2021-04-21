package com.team03.cocktailrecipesapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule


import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import org.junit.Assert


//"../../com.team03.cocktailrecipesapp/ui.login/LoginViewModel"

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private fun validateInput(): Boolean {
        return false;
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)


    @Test
    fun loginDataTest(){
        onView(withId(R.id.etUsername)).perform(typeText("Testusername"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("Testpassword"), closeSoftKeyboard())
    }

    @Test
    fun loginButton() {
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun registerButton() {
        onView(withId(R.id.btnRegister)).perform(click())
    }

    @Test
    fun fillOutRegistrationform() {
        onView(withId(R.id.btnSwitchToLogin)).perform(click())
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("John Doe"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("password1"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("password1"), closeSoftKeyboard())
        Assert.assertEquals(validateInput(), true);
    }

    @Test
    fun fillOutRegistrationform_ErrorPasswordRepeat() {
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("John Doe"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("password1"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("differentPassword"), closeSoftKeyboard())
        Assert.assertEquals(validateInput(), false);

    }

    @Test
    fun fillOutRegistrationform_ErrorEmptyPassword() {
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("John Doe"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText(""), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText("password1"), closeSoftKeyboard())
        Assert.assertEquals(validateInput(), false);
    }

    @Test
    fun fillOutRegistrationform_ErrorEmptyPasswordRepeat() {
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).perform(typeText("John Doe"), closeSoftKeyboard())
        onView(withId(R.id.txt_password)).perform(typeText("password1"), closeSoftKeyboard())
        onView(withId(R.id.txt_password_repeat)).perform(typeText(""), closeSoftKeyboard())
        Assert.assertEquals(validateInput(), false);
    }
}