package com.team03.cocktailrecipesapp

import android.content.res.Configuration
import android.view.View
import android.widget.Switch
import java.util.*

class UserSettingsActivity : SharedPreferencesActivity() {
    lateinit var swtLangauge: Switch

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_user_settings)

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



