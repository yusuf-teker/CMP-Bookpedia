package com.plcoding.bookpedia.book.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {
    @Upsert
    suspend fun upsert(book: BookEntity)

    // sürekli observe edip güncel listeyi almak için Flow kullanıyoruz
    @Query("SELECT * FROM favorite_books")
    fun getFavoriteBooks(): Flow<List<BookEntity>> // Flow tanımlarken suspend'e gerek yok

    @Query("SELECT * FROM favorite_books WHERE id = :bookId")
    suspend fun getFavoriteBookById(bookId: String): BookEntity?

    @Query("DELETE FROM favorite_books WHERE id = :bookId")
    suspend fun deleteFavoriteBookById(bookId: String)

}