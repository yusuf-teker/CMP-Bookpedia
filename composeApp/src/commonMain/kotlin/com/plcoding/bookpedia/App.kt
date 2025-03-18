package com.plcoding.bookpedia

 import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.*
 import com.plcoding.bookpedia.book.data.network.KtorRemoteBookDataSource
 import com.plcoding.bookpedia.book.data.repository.DefaultBookRepository
 import org.jetbrains.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListViewModel
 import com.plcoding.bookpedia.core.data.HttpClientFactory
 import io.ktor.client.engine.HttpClientEngine

@Composable
@Preview
fun App(engine: HttpClientEngine) {

    MaterialTheme {
        BookListScreenRoot(
            viewModel = remember { BookListViewModel(
                bookRepository = DefaultBookRepository(
                    KtorRemoteBookDataSource(
                        httpClient = HttpClientFactory.createClient(engine = engine)
                    )
                )
            ) },
            onBookClick = {

            },

        )
    }
}