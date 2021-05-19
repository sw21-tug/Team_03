package com.team03.cocktailrecipesapp


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class ChangeLanguageTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    
    @Test
    fun login() {
        onView(withId(R.id.etUsername)).perform(typeText("daniel"), closeSoftKeyboard())
        onView(withId(R.id.etPassword)).perform(typeText("1234qwer"), closeSoftKeyboard())
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun checkLanguageChangedOnProfile()
    {
        // Only works if app started with language english
        login()
        onView(withId(R.id.imgBtnAvatar)).perform(click())
        onView(withId(R.id.swtlanguage)).perform(click())
        onView(withId(R.id.swtlanguage)).check(matches(withText("Переключиться на английский язык")))
        onView(withId(R.id.txtViewLogout)).check(matches(withText("выход")))
    }

    @Test
    fun checkLanguageChangedOnLogin()
    {
        // Only works if app started with language russian
        onView(withId(R.id.etUsername)).check(matches(withHint("Имя пользователя")))
        onView(withId(R.id.etPassword)).check(matches(withHint("Пароль")))
        onView(withId(R.id.btnLogin)).check(matches(withText("Войти")))
        onView(withId(R.id.btnRegister)).check(matches(withText("Регистрация")))
    }

    @Test
    fun checkLanguageChangedOnMain()
    {
        // Only works if app started with language russian and aleady logged in
        onView(withId(R.id.txtViewRecommendedForYou)).check(matches(withText("Рекомендуется для вас")))
        onView(withId(R.id.txtViewTrendingRecipes)).check(matches(withText("Модные рецепты")))
        onView(withId(R.id.txtViewSeeAll)).check(matches(withText("Смотреть все")))
    }

    @Test
    fun changedLanguageRegistry()
    {
        // Only works if app started with language russian and logged out
        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.txt_username)).check(matches(withHint("Имя пользователя")))
        onView(withId(R.id.txt_password)).check(matches(withHint("Пароль")))
        onView(withId(R.id.txt_password_repeat)).check(matches(withHint("Повторите пароль")))
        onView(withId(R.id.btn_register)).check(matches(withText("Регистрация")))
    }
}