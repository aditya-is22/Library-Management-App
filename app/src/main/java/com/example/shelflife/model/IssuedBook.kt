package com.example.shelflife.model

import java.util.Date

data class IssuedBook(
    val id: String,
    val title: String,
    val category: String,
    val issueDate: Date,
    val dueDate: Date,
    val borrowerName: String,
    val returnedDate: Date? = null,
    val finePerDay: Float = 2.0f // Default fine of ₹2 per day
) {
    fun calculateFine(): Float {
        if (returnedDate != null) {
            val daysLate = ((returnedDate.time - dueDate.time) / (1000 * 60 * 60 * 24)).toInt()
            return if (daysLate > 0) daysLate * finePerDay else 0f
        }
        
        val today = Date()
        val daysLate = ((today.time - dueDate.time) / (1000 * 60 * 60 * 24)).toInt()
        return if (daysLate > 0) daysLate * finePerDay else 0f
    }

    val isOverdue: Boolean
        get() = (returnedDate == null && Date().after(dueDate)) || 
                (returnedDate != null && returnedDate.after(dueDate))
} 