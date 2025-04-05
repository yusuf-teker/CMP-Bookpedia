package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.Serializable

@Serializable(with = BookWorkDtoSerializer::class) // Özel serializer kullan
data class BookWorkDto(
    val description: String,
)