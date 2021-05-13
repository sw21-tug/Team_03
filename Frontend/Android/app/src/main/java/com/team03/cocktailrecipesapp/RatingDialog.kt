package com.team03.cocktailrecipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.team03.cocktailrecipesapp.listener.RateRecipeErrorListener
import com.team03.cocktailrecipesapp.listener.RateRecipeListener
import kotlinx.android.synthetic.main.rating_layout.*
import kotlinx.android.synthetic.main.rating_layout.view.*
import kotlin.properties.Delegates

class RatingDialog  : DialogFragment() {
    lateinit var ratingResult: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.rating_layout, container, false)

        view.cancelRatingBtn.setOnClickListener {
            dismiss()
        }
        view.sendRatingBtn.setOnClickListener {
            val rating = view.findViewById<RatingBar>(R.id.selectRatingBar)
            ratingResult = rating.rating.toInt()

            //Todo: Send rating to server
//            CocktailDetailActivity().rateRecipe()

            Toast.makeText(context, "You rated: $ratingResult", Toast.LENGTH_LONG).show()
            dismiss()
        }
        return view
    }
}