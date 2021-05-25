package com.team03.cocktailrecipesapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.team03.cocktailrecipesapp.listener.Ingrediant
import java.util.*




class UserProfileActivity : SharedPreferencesActivity() {
    lateinit var userNameText: TextView
    lateinit var userImage: ImageView
    lateinit var backButton: Button
    var userNameExtra: String? = null

    lateinit var swtLangauge: Switch

    override fun onStart() {
        super.onStart()
        setContentView(R.layout.activity_user_profile)

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

        userNameText = findViewById(R.id.user_profile_username)
        userImage = findViewById(R.id.user_profile_image)
        backButton = findViewById(R.id.user_profile_back_button)
        backButton.setOnClickListener { onBackPressed() }

        // set username
        val extras = intent.extras
        if (extras != null)
            userNameExtra = extras.getString("username")

        userNameText.setText(userNameExtra)

        // set profile picture according to selected language
        val language_ = shared.getString("Language", "");
        var avatarImgae = findViewById<ImageButton>(R.id.imgBtnAvatar);
        if (language_.equals("kv")) {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
        } else {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar ));
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_profile)
        userNameText = findViewById(R.id.user_profile_username)
        userImage = findViewById(R.id.user_profile_image)
        backButton = findViewById(R.id.user_profile_back_button)
        backButton.setOnClickListener { onBackPressed() }

        // set username
        val extras = intent.extras
        if (extras != null)
            userNameExtra = extras.getString("username")
        
        userNameText.setText(userNameExtra)

        // set profile picture according to selected language
        val language_ = shared.getString("Language", "");
        var avatarImgae = findViewById<ImageButton>(R.id.imgBtnAvatar);
        if (language_.equals("kv")) {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
        } else {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar ));
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