package com.plcoding.bookpedia.book.presentatinon.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentatinon.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun  BookListScreenRoot(
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit,
    modifier: Modifier = Modifier
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookListScreen(
        state = state,
        onAction = {  action ->
            when(action){
                is BookListAction.OnBookClick -> onBookClick(action.book) // Başka ekranla ilgili diye burada
                else -> Unit
            }
            viewModel.onAction(action) // bu ekran ile ilgili olanlar burada
        },
        modifier = modifier
    )
}
@Composable
fun BookListScreen( // ASIL UI IZOLE EDILDI BOYLECE PREVIEWDE GÖZÜKEBİLECEK
    state: BookListState,
    onAction: (BookListAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier.fillMaxSize().background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = CenterHorizontally
    ) {
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(BookListAction.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth().padding(16.dp)
        )

    }


}