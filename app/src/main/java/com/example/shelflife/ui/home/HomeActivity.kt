package com.example.shelflife.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shelflife.R
import com.example.shelflife.databinding.ActivityHomeBinding
import com.example.shelflife.model.Book
import com.example.shelflife.ui.auth.LoginActivity
import com.example.shelflife.ui.favorites.FavoritesFragment
import com.example.shelflife.ui.issued.IssuedBooksFragment
import com.example.shelflife.ui.profile.ProfileFragment
import com.example.shelflife.ui.settings.SettingsFragment
import com.google.android.material.navigation.NavigationView
import com.example.shelflife.api.GoogleBooksService
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var bookAdapter: BookAdapter
    private var allBooks = listOf<Book>()
    private var currentCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNavigationDrawer()
        setupRecyclerView()
        setupCategoryChips()
        setupSearch()
        loadBooks()
        updateNavigationHeader()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    private fun updateNavigationHeader() {
        val headerView = binding.navigationView.getHeaderView(0)
        headerView.findViewById<android.widget.TextView>(R.id.userNameText).text = "Ashutosh Kumar Singh"
        headerView.findViewById<android.widget.TextView>(R.id.userEmailText).text = "aks@gmail.com"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_library -> {
                showLibraryContent()
            }
            R.id.nav_issued_books -> {
                loadFragment(IssuedBooksFragment())
            }
            R.id.nav_favorites -> {
                loadFragment(FavoritesFragment())
            }
            R.id.nav_profile -> {
                loadFragment(ProfileFragment())
            }
            R.id.nav_settings -> {
                loadFragment(SettingsFragment())
            }
            R.id.nav_logout -> {
                logout()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun showLibraryContent() {
        // Show the main library content
        binding.libraryHeader.visibility = android.view.View.VISIBLE
        binding.libraryContent.visibility = android.view.View.VISIBLE
        binding.container.visibility = android.view.View.GONE
        
        // Clear any existing fragments
        supportFragmentManager.fragments.forEach { fragment ->
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }

        // Update toolbar title
        supportActionBar?.title = getString(R.string.nav_library)
    }

    private fun loadFragment(fragment: Fragment) {
        // Hide the search and category sections but keep the toolbar
        binding.libraryHeader.visibility = android.view.View.GONE
        binding.libraryContent.visibility = android.view.View.GONE
        binding.container.visibility = android.view.View.VISIBLE

        // Load the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()

        // Update toolbar title based on fragment
        supportActionBar?.title = when (fragment) {
            is FavoritesFragment -> getString(R.string.nav_favorites)
            is SettingsFragment -> getString(R.string.nav_settings)
            is IssuedBooksFragment -> getString(R.string.nav_issued_books)
            else -> getString(R.string.app_name)
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            onLendClick = { book -> lendBook(book) },
            onReturnClick = { book -> returnBook(book) },
            onFavoriteClick = { book -> toggleFavorite(book) }
        )
        binding.booksRecyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = bookAdapter
        }
    }

    private fun setupCategoryChips() {
        binding.categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
            currentCategory = when (checkedId) {
                R.id.chipAll -> "All"
                R.id.chipFiction -> "Fiction"
                R.id.chipScience -> "Science"
                R.id.chipTechnology -> "Technology"
                R.id.chipComics -> "Comics"
                else -> "All"
            }
            lifecycleScope.launch {
                val books = GoogleBooksService.searchBooks("", currentCategory)
                allBooks = books
                filterBooks()
            }
        }
    }

    private fun setupSearch() {
        var searchJob: kotlinx.coroutines.Job? = null
        
        binding.searchInput.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                // Add a small delay to avoid too many API calls while typing
                kotlinx.coroutines.delay(300)
                val query = text?.toString() ?: ""
                if (query.length >= 3) {
                    val books = GoogleBooksService.searchBooks(query, currentCategory)
                    allBooks = books
                    filterBooks()
                } else if (query.isEmpty()) {
                    loadBooks()
                }
            }
        }
    }

    private fun loadBooks() {
        lifecycleScope.launch {
            val books = GoogleBooksService.searchBooks("", currentCategory)
            allBooks = books
            filterBooks()
        }
    }

    private fun filterBooks(searchQuery: String = "") {
        var filteredBooks = allBooks
        
        // Apply search filter if needed (for local filtering only)
        if (searchQuery.isNotEmpty()) {
            filteredBooks = filteredBooks.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }
        }

        bookAdapter.submitList(filteredBooks)
    }

    // Make these methods public so they can be accessed from FavoritesFragment
    fun lendBook(book: Book) {
        if (book.availableQuantity <= 0) return

        val updatedBooks = allBooks.map {
            if (it.id == book.id && it.availableQuantity > 0) {
                it.copy(issuedQuantity = it.issuedQuantity + 1)
            } else {
                it
            }
        }
        allBooks = updatedBooks
        filterBooks()
    }

    fun returnBook(book: Book) {
        val updatedBooks = allBooks.map {
            if (it.id == book.id && it.issuedQuantity > 0) {
                it.copy(issuedQuantity = it.issuedQuantity - 1)
            } else {
                it
            }
        }
        allBooks = updatedBooks
        filterBooks()
    }

    fun toggleFavorite(book: Book) {
        val updatedBooks = allBooks.map {
            if (it.id == book.id) {
                it.copy(isFavorite = !it.isFavorite)
            } else {
                it
            }
        }
        allBooks = updatedBooks
        filterBooks()
    }

    fun getFavoriteBooks(): List<Book> {
        return allBooks.filter { it.isFavorite }
    }

    // Add method to get all books for statistics
    fun getAllBooks(): List<Book> = allBooks

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        getSharedPreferences("auth", MODE_PRIVATE)
            .edit()
            .putBoolean("remember_me", false)
            .apply()
        startActivity(Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    companion object {
        const val DARK_MODE_PREF = "dark_mode"
    }
} 