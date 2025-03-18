package com.plcoding.bookpedia.previews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListScreen
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListState
import com.plcoding.bookpedia.book.presentatinon.book_list.components.BookList
import com.plcoding.bookpedia.book.presentatinon.book_list.components.BookListItem
import com.plcoding.bookpedia.book.presentatinon.book_list.components.BookSearchBar

public val bookList = (1..100).map {
    Book(
        id = it.toString(),
        title = "Kotlin ${it.toString()}",
        authors = listOf("JetBrains"),
        averageRating = 4.5,
        imageUrl = "https://picsum.photos/200",
        description = "Kotlin is a programming language",
        languages = listOf("English"),
        firstPublishedYear = "2010",
        numEditions = 10,
        numPages = 100,
        ratingCount = 100
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun BookSearchBarPreview() {
    MaterialTheme {
        BookSearchBar(
            searchQuery = "Kotlin",
            onSearchQueryChange = {},
            onImeSearch = {},
            modifier = Modifier.fillMaxWidth()
        )
    }

}
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun BookListItemPreview() {
    MaterialTheme {
        BookListItem(
            book = bookList.get(0),
            onClick = {}
        )

    }

}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun BookListPreview() {
    MaterialTheme {
        BookList(
            books = bookList,
            onBookClick = {}
        )

    }

}

@Preview
@Composable
fun BookListScreenPreview(){
    BookListScreen(
        state = BookListState(
            searchResults = bookList
        ),
        onAction = {}
    )
}


