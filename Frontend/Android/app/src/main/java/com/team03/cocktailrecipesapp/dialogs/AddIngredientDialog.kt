package com.team03.cocktailrecipesapp.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.team03.cocktailrecipesapp.IngredientInterface
import kotlinx.android.synthetic.main.ingrediants_dialog_addingrediant.*
import kotlinx.android.synthetic.main.ingrediants_dialog_addingrediant.view.*

class AddIngredientDialog : DialogFragment() {
    var name: String = ""
    private var mCallback: IngredientInterface? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(com.team03.cocktailrecipesapp.R.layout.ingrediants_dialog_addingrediant, container,false)

        view.btnCancelIngrediant.setOnClickListener {
            dismiss()
        }
        view.btnSaveIngrediant.setOnClickListener {
            name = etRecipeName.text.toString()
            Toast.makeText(context, "$name ${getString(com.team03.cocktailrecipesapp.R.string.added)}", Toast.LENGTH_LONG).show()
            mCallback?.onSelectedData(name)
            dismiss()
        }
        return view
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            mCallback = activity as IngredientInterface
        } catch (e: ClassCastException) {
        }
    }
}