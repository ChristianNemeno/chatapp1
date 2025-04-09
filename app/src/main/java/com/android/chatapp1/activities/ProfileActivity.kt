package com.android.chatapp1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.chatapp1.R
import com.android.chatapp1.classes.ChatApplication

class ProfileActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        updateButton = findViewById(R.id.updateButton)
        logoutButton = findViewById(R.id.logoutButton)

        val currentUser = ChatApplication.getCurrentLoggedInUser() ?: ""
        usernameEditText.setText(currentUser)

        updateButton.setOnClickListener {
            val newUsername = usernameEditText.text.toString()
            val newPassword = passwordEditText.text.toString()
            if (newUsername.isNotEmpty() && newPassword.isNotEmpty()) {
                if (ChatApplication.updateProfile(newUsername, newPassword)) {
                    Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            ChatApplication.logoutUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}