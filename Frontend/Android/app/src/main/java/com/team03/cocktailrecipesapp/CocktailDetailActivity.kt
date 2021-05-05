package com.team03.cocktailrecipesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*

class CocktailDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)
    }

    fun onUnsuccessfullLike(){
        //TODO ERROR_MSG
    }

    fun onSuccessfullLike(){
        if(/*!isLiked*/ true) {
            System.out.println("Liked successfully!\n")
        } else {
            System.out.println("Disliked successfully!\n")
        }

    }

    fun like(user_id: Int, recipe_id: Int): Boolean {
        val server = serverAPI(this)
        val listener = LikeListener(::onSuccessfullLike)
        val error_listener = LikeErrorListener(::onUnsuccessfullLike)
        val answer = server.likeRecipe(user_id, recipe_id,
                listener, error_listener);
        if (answer == 1)
        {
            return false
        }
        return true
    }


}