package com.example.shelflife.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shelflife.databinding.ItemCategoryBinding
import com.example.shelflife.model.CustomCategory

class CategoryAdapter(
    private val onCategoryClick: (CustomCategory) -> Unit
) : ListAdapter<CustomCategory, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CustomCategory) {
            binding.apply {
                categoryName.text = category.name
                categoryDescription.text = "${category.books.size} books"
                root.setOnClickListener { onCategoryClick(category) }
            }
        }
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<CustomCategory>() {
        override fun areItemsTheSame(oldItem: CustomCategory, newItem: CustomCategory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CustomCategory, newItem: CustomCategory): Boolean {
            return oldItem == newItem
        }
    }
} 