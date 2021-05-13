package com.team03.cocktailrecipesapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LanguageChangeTest
{
    @get:Rule
    val activityRule = ActivityScenarioRule(UserSettingsActivity::class.java)

    @Test
    fun changedLanguageTest()
    {
        //onView(ViewMatchers.withId(R.id.txtExample)).check(ViewAssertions.matches(ViewMatchers.withText("пример теста")))
        onView(ViewMatchers.withId(R.id.swtlanguage)).check(ViewAssertions.matches(ViewMatchers.withText("Переключиться на английский язык")))
    }

    @Test
    fun changedLanguageMainActivity()
    {
        onView(ViewMatchers.withId(R.id.txtWelcomeUsername)).check(ViewAssertions.matches(ViewMatchers.withText("Добро пожаловать")))
    }

    @Test
    fun changedLanguageLogin()
    {
        onView(ViewMatchers.withId(R.id.etUsername)).check(ViewAssertions.matches(ViewMatchers.withText("Имя пользователя")))
    }

    @Test
    fun changedLanguageRegistry()
    {
        onView(ViewMatchers.withId(R.id.btn_register)).check(ViewAssertions.matches(ViewMatchers.withText("Зарегистрироваться")))
    }


}