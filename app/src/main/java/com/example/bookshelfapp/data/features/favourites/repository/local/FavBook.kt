package com.example.bookshelfapp.data.features.favourites.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bookshelfapp.data.features.books.repository.remote.model.BooksItem

@Entity(tableName = "fav_books")
data class FavBook(
    @PrimaryKey
    val bookId: String,
    val book: BooksItem,
    val isFavBook: Boolean,
)
