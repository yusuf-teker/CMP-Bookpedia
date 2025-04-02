package com.plcoding.bookpedia.book.presentatinon.book_detail

import com.plcoding.bookpedia.book.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null,
)
