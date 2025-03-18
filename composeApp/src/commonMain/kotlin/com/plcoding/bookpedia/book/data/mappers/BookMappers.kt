package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book


// data model to domain model
fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id,
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