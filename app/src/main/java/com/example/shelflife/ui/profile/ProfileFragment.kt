package com.example.shelflife.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shelflife.databinding.FragmentProfileBinding
import com.example.shelflife.model.User
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Load user data (in a real app, this would come from a database or API)
        loadUserData()
        
        // Set up save button
        binding.saveButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        // In a real app, this would load from a database or API
        // For now, we'll use mock data
        currentUser = User(
            email = requireActivity().getSharedPreferences("auth", 0)
                .getString("user_email", "aks@gmail.com") ?: "aks@gmail.com",
            name = "Ashutosh Singh",
            designation = "Head Librarian",
            experience = "10 years",
            rating = 4.0f,
            bio = "Passionate about organizing and maintaining library resources.  I want to create a space where people can get away from themselves."
        )

        // Populate the UI
        with(binding) {
            emailInput.setText(currentUser.email)
            nameInput.setText(currentUser.name)
            designationInput.setText(currentUser.designation)
            experienceInput.setText(currentUser.experience)
            bioInput.setText(currentUser.bio)
            ratingBar.rating = currentUser.rating
        }
    }

    private fun saveUserData() {
        // Update the current user object with new values
        currentUser = currentUser.copy(
            name = binding.nameInput.text.toString(),
            designation = binding.designationInput.text.toString(),
            experience = binding.experienceInput.text.toString(),
            bio = binding.bioInput.text.toString()
        )

        // In a real app, save to database or API here
        
        Snackbar.make(binding.root, "Profile updated successfully", Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 