package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

interface BookRepository {
    // Presentationda data packageından birşey kullanmamak için interface kullandık
    // Viewmodelde bu Interface parametre olarak verilecek
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
}