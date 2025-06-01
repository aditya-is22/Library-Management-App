package com.example.shelflife.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.shelflife.databinding.FragmentSettingsBinding
import com.example.shelflife.ui.auth.LoginActivity
import com.example.shelflife.ui.home.HomeActivity

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSettings()
        updateIssuedBooksStats()
    }

    private fun setupSettings() {
        // Remember Me switch
        binding.rememberMeSwitch.isChecked = getRememberMePreference()
        binding.rememberMeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveRememberMePreference(isChecked)
        }

        // Dark Mode switch
        binding.darkModeSwitch.isChecked = getDarkModePreference()
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveDarkModePreference(isChecked)
            updateDarkMode(isChecked)
        }

        // Notifications switch
        binding.notificationsSwitch.isChecked = getNotificationsPreference()
        binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveNotificationsPreference(isChecked)
        }

        // Logout button
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun updateIssuedBooksStats() {
        val homeActivity = activity as? HomeActivity ?: return
        val books = homeActivity.getAllBooks()
        
        // Calculate issued books by category
        val issuedByCategory = books.groupBy { it.category }
            .mapValues { (_, books) -> books.sumOf { it.issuedQuantity } }
        
        // Update UI
        binding.issuedFictionCount.text = "Fiction: ${issuedByCategory["Fiction"] ?: 0}"
        binding.issuedScienceCount.text = "Science: ${issuedByCategory["Science"] ?: 0}"
        binding.issuedTechnologyCount.text = "Technology: ${issuedByCategory["Technology"] ?: 0}"
        binding.issuedComicsCount.text = "Comics: ${issuedByCategory["Comics"] ?: 0}"
        
        // Calculate and display total
        val totalIssued = issuedByCategory.values.sum()
        binding.totalIssuedCount.text = "Total Issued: $totalIssued"
    }

    override fun onResume() {
        super.onResume()
        updateIssuedBooksStats()
    }

    private fun getRememberMePreference(): Boolean {
        return requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .getBoolean("remember_me", false)
    }

    private fun saveRememberMePreference(remember: Boolean) {
        requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("remember_me", remember)
            .apply()
    }

    private fun getDarkModePreference(): Boolean {
        return requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getBoolean(HomeActivity.DARK_MODE_PREF, false)
    }

    private fun saveDarkModePreference(darkMode: Boolean) {
        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putBoolean(HomeActivity.DARK_MODE_PREF, darkMode)
            .apply()
    }

    private fun updateDarkMode(darkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun getNotificationsPreference(): Boolean {
        return requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getBoolean("notifications", true)
    }

    private fun saveNotificationsPreference(enabled: Boolean) {
        requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("notifications", enabled)
            .apply()
    }

    private fun logout() {
        // Clear remember me preference
        saveRememberMePreference(false)
        
        // Start login activity
        startActivity(Intent(requireContext(), LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 