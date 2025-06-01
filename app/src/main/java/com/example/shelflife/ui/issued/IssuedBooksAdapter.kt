package com.example.shelflife.ui.issued

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shelflife.databinding.ItemIssuedBookBinding
import com.example.shelflife.model.IssuedBook
import java.text.SimpleDateFormat
import java.util.Locale

class IssuedBooksAdapter : ListAdapter<IssuedBook, IssuedBooksAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemIssuedBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemIssuedBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        fun bind(book: IssuedBook) {
            binding.apply {
                titleText.text = book.title
                categoryText.text = book.category
                borrowerText.text = "Borrowed by: ${book.borrowerName}"
                issueDateText.text = "Issued: ${dateFormat.format(book.issueDate)}"
                dueDateText.text = "Due: ${dateFormat.format(book.dueDate)}"

                val fine = book.calculateFine()
                if (fine > 0) {
                    fineText.text = "Fine: ₹${"%.2f".format(fine)}"
                    fineText.setTextColor(Color.RED)
                } else {
                    fineText.text = "No fine"
                    fineText.setTextColor(Color.GREEN)
                }

                if (book.isOverdue) {
                    statusText.text = "OVERDUE"
                    statusText.setTextColor(Color.RED)
                } else {
                    statusText.text = "ON TIME"
                    statusText.setTextColor(Color.GREEN)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<IssuedBook>() {
        override fun areItemsTheSame(oldItem: IssuedBook, newItem: IssuedBook) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: IssuedBook, newItem: IssuedBook) =
            oldItem == newItem
    }
} 