package com.example.shelflife.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.shelflife.databinding.ActivitySignupBinding
import com.example.shelflife.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (validateInput(name, email, password, confirmPassword)) {
                handleSignup(name, email, password)
            }
        }

        binding.loginButton.setOnClickListener {
            finish() // Go back to LoginActivity
        }
    }

    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        // Validate name
        if (name.isEmpty()) {
            binding.nameLayout.error = "Name is required"
            isValid = false
        } else {
            binding.nameLayout.error = null
        }

        // Validate email
        if (email.isEmpty()) {
            binding.emailLayout.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Invalid email format"
            isValid = false
        } else {
            binding.emailLayout.error = null
        }

        // Validate password
        if (password.isEmpty()) {
            binding.passwordLayout.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            binding.passwordLayout.error = null
        }

        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordLayout.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordLayout.error = "Passwords do not match"
            isValid = false
        } else {
            binding.confirmPasswordLayout.error = null
        }

        return isValid
    }

    private fun handleSignup(name: String, email: String, password: String) {
        // For demo purposes, we'll just save the user data in SharedPreferences
        // In a real app, you would want to use Firebase Auth or your own backend
        getSharedPreferences("auth", MODE_PRIVATE)
            .edit()
            .apply {
                putBoolean("remember_me", true)
                putString("user_name", name)
                putString("user_email", email)
            }
            .apply()

        // Show success message
        Snackbar.make(binding.root, "Account created successfully!", Snackbar.LENGTH_SHORT).show()

        // Navigate to HomeActivity
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity() // Clear the activity stack
    }
} 