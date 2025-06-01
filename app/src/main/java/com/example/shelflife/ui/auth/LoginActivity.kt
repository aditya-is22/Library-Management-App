package com.example.shelflife.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shelflife.databinding.ActivityLoginBinding
import com.example.shelflife.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        if (getSharedPreferences("auth", MODE_PRIVATE).getBoolean("remember_me", false)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setupViews()
    }

    private fun setupViews() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (validateInput(email, password)) {
                handleLogin(email, password)
            }
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent()
            intent.setClassName(
                "com.example.shelflife",
                "com.example.shelflife.ui.auth.SignupActivity"
            )
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.emailLayout.error = "Email is required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Invalid email format"
            return false
        }
        binding.emailLayout.error = null

        if (password.isEmpty()) {
            binding.passwordLayout.error = "Password is required"
            return false
        }
        if (password.length < 6) {
            binding.passwordLayout.error = "Password must be at least 6 characters"
            return false
        }
        binding.passwordLayout.error = null

        return true
    }

    private fun handleLogin(email: String, password: String) {
        // For demo purposes, accept any valid email/password
        // Save login state and email if "Remember Me" is checked
        getSharedPreferences("auth", MODE_PRIVATE)
            .edit()
            .apply {
                putBoolean("remember_me", binding.rememberMeSwitch.isChecked)
                putString("user_email", "aks@gmail.com") // Always use this email for demo
            }
            .apply()

        // Navigate to HomeActivity
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
} 