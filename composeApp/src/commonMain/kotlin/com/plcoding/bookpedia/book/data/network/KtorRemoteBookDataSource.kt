package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val BASE_URL = "https://openlibrary.org"

class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
): RemoteBookDataSource{
    // RemoteDataSourcedan türetme sebebi ilerde farklı bir data source gelirse
    // sadece data layera yeni bir data source ekleyip aynı interfaceden türeteceğiz
    // ve diğer layerlara dokunmamıs olacahız
    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?,
    ): Result< SearchResponseDto, DataError.Remote> { // apiden gelen data modeli sonradan domaindaki modele dönüstürülmed

        return safeCall { // safe call transform response to Result
            httpClient.get(urlString = "$BASE_URL/search.json"){ // get raw Response from api
                // api cagrısına ekstra özellik ekleme
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter("fields", fields)
            }
        }

    }


}

// İstenen alanlar // SearchedBookDto icindeki bilgiler
val fields = listOf(
    "key",
    "title",
    "language",
    "author_key",
    "cover_i",
    "author_name",
    "cover_edition_key",
    "first_publish_year",
    "ratings_average",
    "ratings_count",
    "number_of_pages_median",
    "edition_count"
).joinToString(",")
