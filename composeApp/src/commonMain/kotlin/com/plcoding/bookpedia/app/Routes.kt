package com.plcoding.bookpedia.app

import kotlinx.serialization.Serializable

sealed interface Routes { // sealed ile bu dosya dısınde route eklenemiyor

    @Serializable
    data object BookGraph: Routes

    @Serializable
    data object BookList : Routes //data olmasının sebebi toString olması otomatik

    @Serializable
    data class BookDetail(val bookId: String) : Routes

}