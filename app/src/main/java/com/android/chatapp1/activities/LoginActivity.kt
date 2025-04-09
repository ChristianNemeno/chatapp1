package com.android.chatapp1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.chatapp1.R
import com.android.chatapp1.classes.ChatApplication

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Replace with your layout file name

        usernameEditText = findViewById(R.id.editTextUsernameLogin)
        passwordEditText = findViewById(R.id.editTextPasswordLogin)
        loginButton = findViewById(R.id.buttonLogin)
        errorTextView = findViewById(R.id.textViewErrorLogin)
        registerTextView = findViewById(R.id.textViewRegisterLink)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (ChatApplication.loginUser(username, password)) {
                    ChatApplication.setCurrentLoggedInUser(username) // Set the logged-in user
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java)) // Replace with your HomeActivity
//                    finish()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish() // This is still good to include, but the flags above will ensure the stack is clear
                } else {
                    errorTextView.text = "Incorrect username or password."
                    errorTextView.visibility = TextView.VISIBLE
                }
            } else {
                errorTextView.text = "Please enter both username and password."
                errorTextView.visibility = TextView.VISIBLE
            }
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}