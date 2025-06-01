package com.example.shelflife.model

data class Book(
    val id: String,
    val title: String,
    val category: String,
    val coverUrl: String,
    val totalQuantity: Int,
    val issuedQuantity: Int = 0,
    val isFavorite: Boolean = false,
    val customCategories: List<String> = emptyList() // List of category IDs
) {
    val availableQuantity: Int
        get() = totalQuantity - issuedQuantity

    companion object {
        const val COLLECTION = "books"
    }
} 