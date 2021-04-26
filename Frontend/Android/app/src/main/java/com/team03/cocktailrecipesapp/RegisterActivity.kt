package com.team03.cocktailrecipesapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


    }

    fun login(): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
            etUsername.error = "Should not be blank";
        }

        else if (validateInputRet == -2) {
            etPassword.error = "Should not be blank";
        }

        else if (validateInputRet == -3) {
            etPasswordRepeated.error = "Should not be blank";
        }

        else if (validateInputRet == -4) {
            etPasswordRepeated.error = "Passwords should be the same"
        }
        else
        {
            retVal = true
        }
        return retVal
    }

    fun registerOnClick(view: View) {
        val btnRegister: Button = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener {
            if (performRegister()) {
                login();
            }
        }
    }
}

class RegisterFuncs
{
    fun validateInput(unsername: String, password: String, passwordRepeat: String): Int {
        if (unsername == "") {
            return -1
        }

        else if (password == "") {
            return -2
        }

        else if (passwordRepeat == "") {
            return -3
        }

        else if (passwordRepeat != password) {
            return -4
        }
        else
        {
            return 0;
        }
    }
}