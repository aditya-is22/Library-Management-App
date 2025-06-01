package com.example.shelflife.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shelflife.R
import com.example.shelflife.databinding.ItemBookBinding
import com.example.shelflife.model.Book

class BookAdapter(
    private val onLendClick: (Book) -> Unit,
    private val onReturnClick: (Book) -> Unit,
    private val onFavoriteClick: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.apply {
                bookTitle.text = book.title
                bookCategory.text = book.category
                bookQuantity.text = "Available: ${book.availableQuantity}"

                // Load book cover image from Google Books API URL
                if (book.coverUrl.isNotEmpty()) {
                    Glide.with(bookCover)
                        .load(book.coverUrl)
                        .placeholder(R.drawable.ic_book_placeholder)
                        .error(R.drawable.ic_book_placeholder)
                        .centerCrop()
                        .into(bookCover)
                } else {
                    bookCover.setImageResource(R.drawable.ic_book_placeholder)
                }

                // Set favorite button icon and click listener
                favoriteButton.setIconResource(
                    if (book.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_border
                )
                favoriteButton.setOnClickListener {
                    onFavoriteClick(book)
                }

                // Disable lend button if no books available
                lendButton.isEnabled = book.availableQuantity > 0
                
                lendButton.setOnClickListener {
                    onLendClick(book)
                }
                
                returnButton.setOnClickListener {
                    onReturnClick(book)
                }
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
} 