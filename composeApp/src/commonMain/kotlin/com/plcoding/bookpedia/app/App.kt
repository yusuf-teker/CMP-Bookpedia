package com.plcoding.bookpedia.app

 import androidx.compose.material3.MaterialTheme

 import androidx.compose.runtime.*
 import androidx.lifecycle.ViewModel
 import androidx.lifecycle.compose.collectAsStateWithLifecycle
 import androidx.navigation.NavBackStackEntry
 import androidx.navigation.NavController
 import androidx.navigation.compose.NavHost
 import androidx.navigation.compose.composable
 import androidx.navigation.compose.rememberNavController
 import androidx.navigation.navigation
 import androidx.navigation.toRoute
 import com.plcoding.bookpedia.book.presentatinon.SelectedBookViewModel
 import com.plcoding.bookpedia.book.presentatinon.book_detail.BookDetailAction
 import com.plcoding.bookpedia.book.presentatinon.book_detail.BookDetailViewModel
 import com.plcoding.bookpedia.book.presentatinon.book_detail.BookDetailScreenRoot
 import org.jetbrains.compose.ui.tooling.preview.Preview
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListViewModel
 import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {

    MaterialTheme {

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Routes.BookGraph
        ){
            navigation<Routes.BookGraph>(
                startDestination = Routes.BookList
            ){

                composable<Routes.BookList> { entry ->

                    val viewModel = koinViewModel<BookListViewModel>()
                    val sharedViewModel = entry.sharedKoinViewModel<SelectedBookViewModel>(navController = navController)

                    LaunchedEffect(true){ // Liste ekranı her açıldığında seçileni sil
                        sharedViewModel.onSelectBook(null)
                    }



                    BookListScreenRoot(
                        viewModel = viewModel,
                        onBookClick = { book ->

                            //SharedViewModeli güncelle
                            sharedViewModel.onSelectBook(book)

                            navController.navigate(
                                Routes.BookDetail(book.id)
                            )

                        }
                    )

                }
                composable<Routes.BookDetail> { entry ->
                    val args = entry.toRoute<Routes.BookDetail>() // Detaile gelen argumanlar
                    val sharedViewModel = entry.sharedKoinViewModel<SelectedBookViewModel>(navController = navController)
                    //SharedViewModelde book tıklandığında güncellenecek
                    val selectedBook by sharedViewModel.selectedBook.collectAsStateWithLifecycle()

                    val detailViewModel = koinViewModel<BookDetailViewModel>()

                    LaunchedEffect(selectedBook){
                        selectedBook?.let {
                            detailViewModel.onAction(BookDetailAction.OnSelectedBookChange(it))
                        }
                    }

                    BookDetailScreenRoot(
                        viewModel = detailViewModel,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )



                }
            }
        }

    }
}

@Composable // ekrana bağlı parentın viewModel'ını(yani shared olanı çünkü shared parenta bağlıdır) döndürür
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    // Ekranın(NavBackStackEntry) bağlı olduğu Graphı bul
    // Graphın NavBackStackEntry'sini bul
    // NavBackStackEntry aynı zamanda bir ViewModelStoreOwner'dur yani
        // navBackStakEntry'sini(ViewModelStoreOwner) bildiğimiz ekrana bağlı ViewModeli bul

    val navGraphRout = this.destination.parent?.route ?: return koinViewModel<T> ()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRout)
    }

    return koinViewModel(
        viewModelStoreOwner = parentEntry,
    )
}