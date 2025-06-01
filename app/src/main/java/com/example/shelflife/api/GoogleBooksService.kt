package com.example.shelflife.api

import com.example.shelflife.model.Book
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 40,
        @Query("startIndex") startIndex: Int = 0
    ): BooksResponse
}

data class BooksResponse(
    val items: List<VolumeInfo> = emptyList(),
    val totalItems: Int = 0
)

data class VolumeInfo(
    val id: String,
    val volumeInfo: BookInfo
)

data class BookInfo(
    val title: String,
    val authors: List<String>? = null,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null
)

data class ImageLinks(
    val thumbnail: String? = null
)

object GoogleBooksService {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(GoogleBooksApi::class.java)

    suspend fun searchBooks(query: String, category: String = ""): List<Book> {
        val searchQuery = when {
            query.isNotEmpty() && category.isNotEmpty() && category != "All" -> 
                "$query+subject:$category"
            query.isNotEmpty() -> query
            category.isNotEmpty() && category != "All" -> 
                "subject:$category"
            else -> "subject:fiction" // Default search for popular fiction books
        }

        return try {
            val response = api.searchBooks(searchQuery)
            response.items.map { volume ->
                Book(
                    id = volume.id,
                    title = volume.volumeInfo.title,
                    category = volume.volumeInfo.categories?.firstOrNull() ?: "Uncategorized",
                    coverUrl = volume.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:") ?: "",
                    totalQuantity = (2..8).random(), // Simulating inventory
                    issuedQuantity = 0
                )
            }
        } catch (e: Exception) {
            e.printStackTrace() // Add this for debugging
            emptyList()
        }
    }
} 