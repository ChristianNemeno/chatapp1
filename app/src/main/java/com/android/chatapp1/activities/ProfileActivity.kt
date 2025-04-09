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
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Set up the Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)
        addressEditText = findViewById(R.id.addressEditText)
        ageEditText = findViewById(R.id.ageEditText)
        updateButton = findViewById(R.id.updateButton)
        logoutButton = findViewById(R.id.logoutButton)

        val currentUser = ChatApplication.getCurrentLoggedInUser()
        if (currentUser != null) {
            val userDetails = ChatApplication.getUserDetails(currentUser)
            userDetails?.let {
                usernameEditText.setText(it.username)
                passwordEditText.setText(it.password)
                emailEditText.setText(it.email)
                addressEditText.setText(it.address)
                ageEditText.setText(it.age.toString())
            }
        }

        updateButton.setOnClickListener {
            val newUsername = usernameEditText.text.toString().trim()
            val newPassword = passwordEditText.text.toString().trim()
            val newEmail = emailEditText.text.toString().trim()
            val newAddress = addressEditText.text.toString().trim()
            val newAgeText = ageEditText.text.toString().trim()

            when {
                newUsername.isEmpty() || newPassword.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty() || newAgeText.isEmpty() -> {
                    Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                }
                newPassword.length < 4 -> {
                    Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches() -> {
                    Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                }
                newAgeText.toIntOrNull() == null || newAgeText.toInt() < 0 -> {
                    Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val newAge = newAgeText.toInt()
                    if (ChatApplication.updateProfile(newUsername, newPassword, newEmail, newAddress, newAge)) {
                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        logoutButton.setOnClickListener {
            ChatApplication.logoutUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // Handle back button press
    override fun onSupportNavigateUp(): Boolean {
        finish() // Go back to the previous activity (likely MainActivity)
        return true
    }
}