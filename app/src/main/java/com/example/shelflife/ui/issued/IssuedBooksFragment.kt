package com.example.shelflife.ui.issued

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shelflife.databinding.FragmentIssuedBooksBinding
import com.example.shelflife.model.IssuedBook
import java.util.Calendar
import java.util.Date

class IssuedBooksFragment : Fragment() {
    private var _binding: FragmentIssuedBooksBinding? = null
    private val binding get() = _binding!!
    private lateinit var issuedBooksAdapter: IssuedBooksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIssuedBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadIssuedBooks()
    }

    private fun setupRecyclerView() {
        issuedBooksAdapter = IssuedBooksAdapter()
        binding.issuedBooksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = issuedBooksAdapter
        }
    }

    private fun loadIssuedBooks() {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Create dates for different scenarios
        calendar.add(Calendar.DAY_OF_MONTH, -15) // 15 days ago
        val fifteenDaysAgo = calendar.time

        calendar.add(Calendar.DAY_OF_MONTH, 5) // 10 days ago
        val tenDaysAgo = calendar.time

        calendar.add(Calendar.DAY_OF_MONTH, 5) // 5 days ago
        val fiveDaysAgo = calendar.time

        calendar.time = today
        calendar.add(Calendar.DAY_OF_MONTH, 5) // 5 days from now
        val fiveDaysFromNow = calendar.time

        calendar.add(Calendar.DAY_OF_MONTH, 5) // 10 days from now
        val tenDaysFromNow = calendar.time

        val issuedBooks = listOf(
            IssuedBook(
                id = "1",
                title = "Clean Code",
                category = "Technology",
                issueDate = fifteenDaysAgo,
                dueDate = fiveDaysAgo,
                borrowerName = "Aditya Sharma"
            ),
            IssuedBook(
                id = "2",
                title = "1984",
                category = "Fiction",
                issueDate = tenDaysAgo,
                dueDate = fiveDaysFromNow,
                borrowerName = "Alok Kumar"
            ),
            IssuedBook(
                id = "3",
                title = "The Selfish Gene",
                category = "Science",
                issueDate = fifteenDaysAgo,
                dueDate = fiveDaysAgo,
                borrowerName = "Sonu Patel"
            ),
            IssuedBook(
                id = "4",
                title = "Design Patterns",
                category = "Technology",
                issueDate = tenDaysAgo,
                dueDate = tenDaysFromNow,
                borrowerName = "Rahul Verma"
            ),
            IssuedBook(
                id = "5",
                title = "The Great Gatsby",
                category = "Fiction",
                issueDate = fifteenDaysAgo,
                dueDate = fiveDaysAgo,
                borrowerName = "Priya Singh"
            ),
            IssuedBook(
                id = "6",
                title = "A Brief History of Time",
                category = "Science",
                issueDate = fiveDaysAgo,
                dueDate = tenDaysFromNow,
                borrowerName = "Amit Kumar"
            ),
            IssuedBook(
                id = "7",
                title = "The Pragmatic Programmer",
                category = "Technology",
                issueDate = fifteenDaysAgo,
                dueDate = fiveDaysAgo,
                borrowerName = "Neha Gupta"
            ),
            IssuedBook(
                id = "8",
                title = "Pride and Prejudice",
                category = "Fiction",
                issueDate = tenDaysAgo,
                dueDate = fiveDaysFromNow,
                borrowerName = "Ravi Shankar"
            ),
            IssuedBook(
                id = "9",
                title = "The Origin of Species",
                category = "Science",
                issueDate = fifteenDaysAgo,
                dueDate = fiveDaysAgo,
                borrowerName = "Anjali Desai"
            ),
            IssuedBook(
                id = "10",
                title = "Head First Design Patterns",
                category = "Technology",
                issueDate = fiveDaysAgo,
                dueDate = tenDaysFromNow,
                borrowerName = "Vikram Malhotra"
            ),
            IssuedBook(
                id = "11",
                title = "To Kill a Mockingbird",
                category = "Fiction",
                issueDate = fifteenDaysAgo,
                dueDate = fiveDaysAgo,
                borrowerName = "Deepak Chopra"
            ),
            IssuedBook(
                id = "12",
                title = "The Elegant Universe",
                category = "Science",
                issueDate = tenDaysAgo,
                dueDate = fiveDaysFromNow,
                borrowerName = "Meera Reddy"
            ),
            IssuedBook(
                id = "13",
                title = "Code Complete",
                category = "Technology",
                issueDate = fifteenDaysAgo,
                dueDate = fiveDaysAgo,
                borrowerName = "Arjun Nair"
            ),
            IssuedBook(
                id = "14",
                title = "The Catcher in the Rye",
                category = "Fiction",
                issueDate = fiveDaysAgo,
                dueDate = tenDaysFromNow,
                borrowerName = "Kiran Shah"
            ),
            IssuedBook(
                id = "15",
                title = "Cosmos",
                category = "Science",
                issueDate = tenDaysAgo,
                dueDate = fiveDaysFromNow,
                borrowerName = "Rajesh Khanna"
            )
        )

        updateFinesSummary(issuedBooks)
        issuedBooksAdapter.submitList(issuedBooks)
    }

    private fun updateFinesSummary(books: List<IssuedBook>) {
        val finesByCategory = books
            .filter { it.isOverdue }
            .groupBy { it.category }
            .mapValues { (_, books) -> books.sumOf { it.calculateFine().toDouble() } }

        // Clear existing category views
        binding.finesSummaryContainer.removeAllViews()

        // Add category-wise fines
        finesByCategory.forEach { (category, fine) ->
            val categoryText = TextView(context).apply {
                text = "$category: ₹${"%.2f".format(fine)}"
                textSize = 16f
                setPadding(0, 4, 0, 4)
            }
            binding.finesSummaryContainer.addView(categoryText)
        }

        // Update total fines
        val totalFines = finesByCategory.values.sum()
        binding.totalFinesText.text = "Total Pending Fines: ₹${"%.2f".format(totalFines)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 