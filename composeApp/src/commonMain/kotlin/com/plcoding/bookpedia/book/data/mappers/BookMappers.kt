package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.database.BookEntity
import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book


// data model to domain model
fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id.substringAfterLast("/"), // en son /'dan sonraki kısmı alıyor
        title = title,
        imageUrl = if (coverKey != null){
            "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
        }else{
            "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"

        },
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishedYear = firstPublishYear?.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numbPagesMedian,
        numEditions = editionCount
    )
}

fun Book.toBookEntity(): BookEntity{
    return BookEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        language = languages,
        authors = authors,
        firstPublishYear = firstPublishedYear,
        ratingAverage = averageRating,
        ratingCount = ratingCount,
        numPagesMedian = numPages,
        numEditions = numEditions,
    )
}

fun BookEntity.toBook(): Book{
    return Book(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl ?: "",
        languages = language,
        authors = authors,
        firstPublishedYear = firstPublishYear ,
        averageRating  = ratingAverage,
        ratingCount = ratingCount,
        numPages  = numPagesMedian,
        numEditions = numEditions,
    )
}