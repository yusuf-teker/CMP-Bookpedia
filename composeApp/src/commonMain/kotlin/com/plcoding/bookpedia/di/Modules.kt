package com.plcoding.bookpedia.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.plcoding.bookpedia.book.data.database.DatabaseFactory
import com.plcoding.bookpedia.book.data.database.FavoriteBookDatabase
import com.plcoding.bookpedia.book.data.network.KtorRemoteBookDataSource
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.data.repository.DefaultBookRepository
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.presentatinon.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentatinon.book_detail.BookDetailViewModel
import com.plcoding.bookpedia.book.presentatinon.book_list.BookListViewModel
import com.plcoding.bookpedia.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


expect val platformModule: Module

val sharedModule = module {

    single {
        // engine platforma gore degisiyor bu yüzde
        // expect val platformModule: Module tanımlamlı ve tüm platformlar için implemente (actual) etmeliyiz
        HttpClientFactory.createClient(get())
    }

    //httpclient nasıl olustugu yukarıda öğretildi
    //RemoteBookDataSource interfacei türünde bir veri Inject edildiğinde
        //KtorRemoteBookDataSource sınıfı kullanılacak
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()

    //RemoteBookDataSource nasıl tanımlandıgı yukarıda gösterildi
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single { // Create Database
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { // Create FavoriteBookDao
        get<FavoriteBookDatabase>().favoriteBookDao
    }

    viewModel { BookListViewModel(get()) }

    viewModel { SelectedBookViewModel() }

    viewModel { BookDetailViewModel(get(), get()) }
}