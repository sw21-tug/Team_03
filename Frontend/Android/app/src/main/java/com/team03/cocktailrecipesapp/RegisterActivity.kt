package com.team03.cocktailrecipesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.team03.cocktailrecipesapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onUnsuccessfullRegister(){
        etUsername.error = "Username already in use";
    }

    fun onSuccessfullRegister(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun register(username: String, password: String): Boolean {
        val server = serverAPI(this)
        val pwHash = CryptoUtils.getSHA512(password)
        val listener = RegisterListener(::onSuccessfullRegister)
        val error_listener = RegisterErrorListener(::onUnsuccessfullRegister)
        val answer = server.register(username, pwHash,
                listener, error_listener);
        if (answer == 1)
        {
            return false
        }
        return true
    }

    fun performRegister(): Boolean {
        val etUsername: EditText = findViewById(R.id.txt_username);
        val etPassword: EditText = findViewById(R.id.txt_password);
        val etPasswordRepeated: EditText = findViewById(R.id.txt_password_repeat);
        var retVal: Boolean
        retVal = false;

        var validateInputRet: Int = RegisterFuncs().validateInput(etUsername.text.toString(), etPassword.text.toString(), etPasswordRepeated.text.toString())


        if (validateInputRet == -1) {
            etUsername.error = resources.getString(R.string.should_not_be_blank);
        }

        else if (validateInputRet == -2) {
            etPassword.error = resources.getString(R.string.should_not_be_blank);
        }

        else if (validateInputRet == -3) {
            etPasswordRepeated.error = resources.getString(R.string.should_not_be_blank);
        }

        else if (validateInputRet == -4) {
            etPasswordRepeated.error = resources.getString(R.string.passwords_should_be_the_same)
        }
        else if (validateInputRet == -5) {
            etUsername.error = resources.getString(R.string.username_to_less_characters)
        }
        else if (validateInputRet == -6) {
            etPassword.error = resources.getString(R.string.password_to_less_characters)
        }
        else
        {
            retVal = true
        }
        return retVal
    }

    fun registerOnClick(view: View) {
        if (performRegister()) {
            val etUsername: EditText = findViewById(R.id.txt_username);
            val etPassword: EditText = findViewById(R.id.txt_password);
            register(etUsername.text.toString(), etPassword.text.toString());
        }
    }
}

class RegisterFuncs
{
    fun validateInput(username: String, password: String, passwordRepeat: String): Int {
        var pattern = Regex("\\w{3,}")
        if (username == "") {
            return -1
        }
        else if (password == "") {
            return -2
        }

        else if (passwordRepeat == "") {
            return -3
        }
        else if (password.length < 5)
        {
            return -6
        }
        else if (passwordRepeat != password) {
            return -4
        }
        else if (pattern.containsMatchIn(username) == false)
        {
            return -5
        }

        else
        {
            return 0;
        }
    }
}
