package com.android.chatapp1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.chatapp1.R
import com.android.chatapp1.classes.ChatApplication

class RegistrationActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)
        addressEditText = findViewById(R.id.addressEditText)
        ageEditText = findViewById(R.id.ageEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val ageText = ageEditText.text.toString().trim()

            when {
                username.isEmpty() || password.isEmpty() || email.isEmpty() || address.isEmpty() || ageText.isEmpty() -> {
                    showToast("Please fill in all fields.")
                }
                password.length < 4 -> {
                    showToast("Password must be at least 4 characters long.")
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showToast("Please enter a valid email address.")
                }
                ageText.toIntOrNull() == null || ageText.toInt() < 0 -> {
                    showToast("Please enter a valid age.")
                }
                else -> {
                    val age = ageText.toInt()
                    if (ChatApplication.registerUser(username, password, email, address, age)) {
                        showToast("Registration successful!")
                        navigateToLogin()
                    } else {
                        showToast("Username already exists.")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}