package com.example.shelflife.model

data class User(
    val email: String,
    var name: String,
    var designation: String,
    var experience: String,
    val rating: Float,
    var bio: String = ""
) 