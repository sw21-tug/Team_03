package com.team03.cocktailrecipesapp.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.team03.cocktailrecipesapp.*

//import com.team03.cocktailrecipesapp.userLoggedIn

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val login = findViewById<Button>(R.id.btnLogin)
        val loading = findViewById<ProgressBar>(R.id.loading)
        val error = findViewById<TextView>(R.id.loginResponseMessage)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            loading.visibility = View.INVISIBLE

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
               password.error = getString(loginState.passwordError)
            }
            if (loginState.isServerError != null) {
                error.visibility = View.VISIBLE
                error.text = getString(loginState.isServerError)
            }
            if (loginState.isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success) {
                updateUiWithUser()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            if (error.visibility == View.VISIBLE)
                error.visibility = View.INVISIBLE

            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            if (error.visibility == View.VISIBLE)
                error.visibility = View.INVISIBLE
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString(),
                            context
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString(), context)
            }
        }
    }

    override fun onBackPressed() {
    }

    fun registerOnClickFromLogin(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience

        Toast.makeText(
            applicationContext,
            "$welcome $userName",
            Toast.LENGTH_LONG
        ).show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}