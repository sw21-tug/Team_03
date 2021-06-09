package com.team03.cocktailrecipesapp.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.team03.cocktailrecipesapp.R
import com.team03.cocktailrecipesapp.RatingInterface
import kotlinx.android.synthetic.main.rating_layout.view.*

class RatingDialog  : DialogFragment() {
    var ratingResult: Int = 0
    private var mCallback: RatingInterface? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.rating_layout, container, false)

        view.cancelRatingBtn.setOnClickListener {
            dismiss()
        }
        view.sendRatingBtn.setOnClickListener {
            val rating = view.findViewById<RatingBar>(R.id.selectRatingBar)
            ratingResult = rating.rating.toInt()
            Toast.makeText(context, getString(R.string.rated) + ": " + ratingResult.toString(), Toast.LENGTH_LONG).show()
            mCallback?.onSelectedData(ratingResult)
            dismiss()
        }
        return view
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            mCallback = activity as RatingInterface
        } catch (e: ClassCastException) {
        }
    }
}