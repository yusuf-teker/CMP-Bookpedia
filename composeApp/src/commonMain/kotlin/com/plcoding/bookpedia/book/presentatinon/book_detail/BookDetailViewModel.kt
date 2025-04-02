package com.plcoding.bookpedia.book.presentatinon.book_detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookDetailViewModel:  ViewModel() {
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: BookDetailAction){
        when(action){
            BookDetailAction.OnFavoriteClick -> {}
            is BookDetailAction.OnSelectedBookChange -> {
                _state.value = _state.value.copy(
                    book = action.book
                )

            }
            BookDetailAction.OnBackClick -> {}
        }
    }
}