package com.plcoding.bookpedia.book.presentatinon.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.favorites
import cmp_bookpedia.composeapp.generated.resources.no_favorite_books
import cmp_bookpedia.composeapp.generated.resources.no_result
import cmp_bookpedia.composeapp.generated.resources.search_results
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.presentatinon.book_list.components.BookList
import com.plcoding.bookpedia.book.presentatinon.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.SandYellow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
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
                is BookListAction.OnBookClick -> {

                    onBookClick(action.book)
                } // Başka ekranla ilgili diye burada
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
    val pagerState = rememberPagerState( 0, pageCount = {2} )
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }
    val scope = rememberCoroutineScope()

    val searchResultListState = rememberLazyListState()
    LaunchedEffect(state.searchResults){
        searchResultListState.scrollToItem(0) // liste değişince başa gelmesi için
    }
    val favoriteListState = rememberLazyListState()
    LaunchedEffect(state.favoriteBooks){
        favoriteListState.scrollToItem(0)
    }
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

        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize()
            ) {
                TabRow( // sadece düz tab swipe etmiyor horizontalpager kullanarak swipe ekleyebiliriz
                    selectedTabIndex = selectedTabIndex.value,
                    modifier = Modifier.padding(vertical = 12.dp).widthIn(700.dp).fillMaxWidth(),
                    containerColor = DesertWhite,
                    contentColor = DarkBlue,
                    indicator = { tabPositions ->
                        // yeni Indicator kullanıyoruz Defualt Indicatoru özelleştirdik
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                            color = SandYellow
                        )
                    }
                ){
                    Tab(
                        selected = selectedTabIndex.value == 0,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                        text = {
                            Text(
                                text = stringResource(Res.string.search_results),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex.value == 1,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                        text = {
                            Text(
                                text = stringResource(Res.string.favorites),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    )
                }

                Spacer(modifier.height(4.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ){ pageIndex ->

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        when(pageIndex){
                        0 -> {
                            if (state.isLoading){
                                CircularProgressIndicator()
                            }else{
                                when{
                                    state.errorMessage != null -> {
                                        Text(
                                            text = state.errorMessage.asString(),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error                                        )
                                    }
                                    state.searchResults.isEmpty() -> {
                                        Text(
                                            text = stringResource(Res.string.no_result),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    else -> {
                                        BookList(
                                            books = state.searchResults,
                                            onBookClick = {
                                                onAction(BookListAction.OnBookClick(it))
                                            },
                                            scrollState = searchResultListState
                                        )
                                    }
                                }

                            }

                        }
                        1 -> {
                            if (state.isLoading){
                                CircularProgressIndicator()
                            }else{
                                when{
                                    state.favoriteBooks.isEmpty() -> {
                                        Text(
                                            text = stringResource(Res.string.no_favorite_books),
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    else -> {
                                        BookList(
                                            books = state.favoriteBooks,
                                            onBookClick = {
                                                onAction(BookListAction.OnBookClick(it))
                                            },
                                            scrollState = favoriteListState
                                        )
                                    }
                                }

                            }
                        }
                    }

                    }


                    
                }




            }




        }

    }


}