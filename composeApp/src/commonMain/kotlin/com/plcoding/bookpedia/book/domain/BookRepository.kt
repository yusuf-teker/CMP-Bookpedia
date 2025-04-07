package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    // Presentationda data packageından birşey kullanmamak için interface kullandık
    // Viewmodelde bu Interface parametre olarak verilecek
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>

    suspend fun getBookDescription(bookId: String): Result<String?, DataError> // Remote dışında local db'dende çekicez

     fun getFavoriteBooks() : Flow<List<Book>>

     fun isBookFavorite(bookId: String): Flow<Boolean>

     suspend fun markAsFavorite(book: Book): Result<Unit, DataError> // EmptyResult

     suspend fun deleteFavoriteBook(bookId: String)

}