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
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize UI components with IDs from activity_registration.xml
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        // Set up register button click listener
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            when {
                username.isEmpty() || password.isEmpty() -> {
                    showToast("Please enter both username and password.")
                }
//                password.length < 6 -> {
//                    showToast("Password must be at least 6 characters long.")
//                }
                else -> {
                    if (ChatApplication.registerUser(username, password)) {
                        showToast("Registration successful!")
                        navigateToLogin()
                    } else {
                        showToast("Username already exists.")
                    }
                }
            }
        }
    }

    /**
     * Displays a toast message to the user.
     * @param message The message to display
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Navigates to the LoginActivity and finishes the current activity.
     */
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}