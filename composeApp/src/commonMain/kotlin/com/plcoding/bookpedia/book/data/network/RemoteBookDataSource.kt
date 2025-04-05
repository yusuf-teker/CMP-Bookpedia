package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

// RemoteDataSourcedan türetme sebebi ilerde farklı bir data source gelirse
// sadece data layera yeni bir data source ekleyip aynı interfaceden türeteceğiz
// ve diğer layerlara dokunmamıs olacahız
interface RemoteBookDataSource {
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null,
    ): Result<SearchResponseDto, DataError.Remote>

    suspend fun getBookDetails(
        bookId: String
    ): Result<BookWorkDto, DataError.Remote>
}