package com.team03.cocktailrecipesapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.net.sip.SipSession
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import com.team03.cocktailrecipesapp.R
import com.team03.cocktailrecipesapp.error_listener.DeleteRecipeErrorListener
import com.team03.cocktailrecipesapp.listener.DeleteRecipeListener
import com.team03.cocktailrecipesapp.serverAPI
import com.team03.cocktailrecipesapp.userId

class DeleteRecipeDialogFragment : DialogFragment() {
    lateinit var server : serverAPI
    lateinit var succesListener : DeleteRecipeListener
    lateinit var errorListener : DeleteRecipeErrorListener
    var recipe_id : Int = -1


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_delete_recipe)
                    .setPositiveButton(R.string.dialog_delete_recipe_delete,
                            DialogInterface.OnClickListener { dialog, id ->

                                server.deleteRecipe(userId, recipe_id, succesListener, errorListener)
                            })
                    .setNegativeButton(R.string.dialog_delete_recipe_cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                // User cancelled the dialog
                            })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}