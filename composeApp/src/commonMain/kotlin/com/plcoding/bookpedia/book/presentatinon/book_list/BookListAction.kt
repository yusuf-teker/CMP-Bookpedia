package com.plcoding.bookpedia.book.presentatinon.book_list

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookListAction { // Action that view can perform
    data class OnSearchQueryChange(val query: String): BookListAction
    data class OnBookClick(val book: Book): BookListAction
    data class OnTabSelected(val index: Int): BookListAction

}