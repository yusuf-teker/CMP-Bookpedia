    package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.Serializable


// API sacma oldugu icin description 2 şekilde gelebiliyiro
// 1- Json içinde Description objesi var içerisinde ki value desctiption
// 2- Json içinde description stringi var
@Serializable()
data class DescriptionDto(
    val value: String? = null
)