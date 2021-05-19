package com.team03.cocktailrecipesapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class UserProfileActivity : SharedPreferencesActivity() {
    lateinit var userNameText: TextView
    lateinit var userImage: ImageView
    lateinit var backButton: Button
    var userNameExtra: String? = null

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
        val language = shared.getString("Language", "");
        var avatarImgae = findViewById<ImageButton>(R.id.imgBtnAvatar);
        if (language.equals("kv")) {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
        } else {
            userImage.setBackground(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar ));
        }
    }
}