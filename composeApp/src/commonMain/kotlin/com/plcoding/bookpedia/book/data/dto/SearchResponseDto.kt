package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SearchResponseDto( // Ana Json Docs icinde geliyor o yuzden
    @SerialName("docs") val results: List<SearchedBookDto>
)