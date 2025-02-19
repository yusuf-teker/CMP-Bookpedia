package com.plcoding.bookpedia

import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        BookListScreenRoot(
            viewModel = remember { BookListViewModel() },
            onBookClick = {

            },

        )
    }
}