package com.plcoding.bookpedia.book.presentatinon.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BookListViewModel(
    private val bookRepository : BookRepository // presentation layerde domain layerı kullandık interface yaratıp

): ViewModel() {


    private  var cachedBooks = emptyList<Book>()

    private var searchJob: Job? = null


    val bookList = (1..100).map {
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

    private val _state = MutableStateFlow(BookListState())
    val state: StateFlow<BookListState> = _state
        .onStart {
            if (cachedBooks.isEmpty()){
                observeSearchQuery()
            }
        }.stateIn( // Flowu(Cold) StateFlowa(Hot) donustur
            viewModelScope, //  bu state viewModele baglandı viewmodel clear olunca state e clear olacak
            SharingStarted.WhileSubscribed(5000L), // 5 saniye dinleyici olmazsa otomatik kapanacak
            initialValue = _state.value // yukarıda BookListState() tanımlanmıs
        )



    fun onAction(action: BookListAction){
        when(action) {
            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }

            is BookListAction.OnBookClick -> {
                // Başka ekranla ilgili o yüzden composablea rootta tanımlandı
            }
            is BookListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery(){
        state.map { it.searchQuery }
            .distinctUntilChanged() //
            .debounce(500) // 500 ms bekledikten sonra api call
            .onEach { query ->
                when{
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedBooks
                            )
                        }
                    }
                    query.length > 2 -> {
                        searchJob?.cancel() // hali hazırda istek varsa ve yeni istek atılırsa eskisini iptal et
                        searchJob = searchBooks(query)
                    }
                    else -> {
                    }
                }
            }.launchIn(viewModelScope) // collect state UI'ın yanı sıra viewmodelde de dinleniyor
    }


    private fun searchBooks(query: String): Job =  viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true,)
            }
            bookRepository.searchBooks(query)
                .onSuccess { searchResults ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            searchResults = searchResults
                        )
                    }

                }.onError { error ->
                    _state.update {
                        it.copy(
                            searchResults = emptyList(),
                            isLoading = true,
                            errorMessage = error.toUiText()
                        )
                    }
                }
        }




}
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