package com.team03.cocktailrecipesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*

class CocktailDetailActivity : AppCompatActivity() {
    var isLiked = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)
    }

    fun onUnsuccessfullLike(){
        //TODO ERROR_MSG
        System.out.println("not successful\n")
    }

    fun onSuccessfullLike(){
        var imgLike = findViewById<ImageButton>(R.id.imageButtonLike);
        if(!isLiked) {
            //TODO change russian to the final heart icon
            imgLike.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.russian_avatar ));
            isLiked = true
            System.out.println("liked\n")
        } else {
            //TODO change russian to the final heart icon
            imgLike.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.default_avatar));
            isLiked = false
            System.out.println("disliked\n")
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

    fun likeOnClick(view: View) {
        run {
            System.out.println("onclick\n")
            like(1, 1);
        }
    }


}