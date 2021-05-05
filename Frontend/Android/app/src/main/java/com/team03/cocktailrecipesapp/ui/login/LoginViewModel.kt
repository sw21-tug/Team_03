package com.team03.cocktailrecipesapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.team03.cocktailrecipesapp.data.LoginRepository
import android.content.Context
import android.widget.TextView
import com.team03.cocktailrecipesapp.*
import org.mindrot.jbcrypt.BCrypt

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun onSuccessfulLogin(user_id: Int) {
        if (user_id >= 0) {
            userId = user_id;
            _loginResult.value = LoginResult(success = true)
        }
        else {
            _loginForm.value = LoginFormState(isServerError = R.string.wrong_username_or_password)
        }
    }

    fun onFailedLogin() {
        System.out.println("Login did not work!");
    }

    fun login(username: String, password: String, context: Context) {
        // can be launched in a separate asynchronous job
        val password_hash = CryptoUtils.getSHA512(password);
        val listener = LoginListener(::onSuccessfulLogin);
        val error_listener = LoginErrorListener(::onFailedLogin);

        val server = serverAPI(context);
        server.login(username, password_hash, listener, error_listener)
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
            userName = username;
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }
}