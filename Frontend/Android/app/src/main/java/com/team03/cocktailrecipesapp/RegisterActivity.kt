package com.team03.cocktailrecipesapp

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class RegisterActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Enables Always-on
        setAmbientEnabled()
    }
}