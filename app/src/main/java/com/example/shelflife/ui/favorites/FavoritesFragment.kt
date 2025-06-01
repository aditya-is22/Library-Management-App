package com.example.shelflife.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shelflife.databinding.FragmentFavoritesBinding
import com.example.shelflife.ui.home.BookAdapter
import com.example.shelflife.ui.home.HomeActivity

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadFavoriteBooks()
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            onLendClick = { book -> (activity as? HomeActivity)?.lendBook(book) },
            onReturnClick = { book -> (activity as? HomeActivity)?.returnBook(book) },
            onFavoriteClick = { book -> (activity as? HomeActivity)?.toggleFavorite(book) }
        )
        binding.favoriteBooksRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = bookAdapter
        }
    }

    private fun loadFavoriteBooks() {
        val favoriteBooks = (activity as? HomeActivity)?.getFavoriteBooks() ?: emptyList()
        bookAdapter.submitList(favoriteBooks)
        
        // Show/hide empty state
        if (favoriteBooks.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.favoriteBooksRecyclerView.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.favoriteBooksRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteBooks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 