package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class UserProfile : SharedPreferencesActivity() {

    lateinit var test: TextView
    lateinit var swtLangauge: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_user_profile)

        test = findViewById(R.id.txtExample)
        swtLangauge = findViewById(R.id.swtlanguage)

        val language: String? = shared.getString("Language", "")
        swtLangauge.isChecked = language.equals("kv")
        swtLangauge.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                setLanguage("kv")
            } else {
                setLanguage("en")
            }
            onStart()
        }
    }

    fun setLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        shared_editor.putString("Language", language).apply()
    }

    fun onLogoutPressed(view: View) {
        shared_editor.putInt("userId", 0).apply()
        userId = 0
        onBackPressed()
    }
}



