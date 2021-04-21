package com.team03.cocktailrecipesapp.data

import com.team03.cocktailrecipesapp.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            // TODO: check in db if user exists and get info --> display name,

            val displayName = "Jane Doe";

            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), displayName)
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

}