package com.example.shelflife.model

data class CustomCategory(
    val id: String,
    val name: String,
    val description: String = "",
    val books: List<String> = emptyList() // List of book IDs
) 