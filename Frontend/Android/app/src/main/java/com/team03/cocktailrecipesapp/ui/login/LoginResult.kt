package com.team03.cocktailrecipesapp.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult (
     val success:Boolean = false,
     val error:Int? = null
)