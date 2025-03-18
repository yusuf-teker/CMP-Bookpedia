package com.plcoding.bookpedia.book.data.repository

import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource
    // Implementation değişebilir ileride fakat bu interface değişmez o yüzden
    //implementation değişse ve farklı bir data source yazılsa bile burayı değiştirmeye gerek kalmaz
    // ve bu durumda domain layerııda değiştirmeye gerek kalmaz
): BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>{ // domain modeli dönüyor artık.
        return remoteBookDataSource
            .searchBooks(query)
            .map { searchResponseDto -> //bizim map fonksiyonumuz Result içindeki datayı alıyor ve aşşagıdaki kodları uyguluyor ve return oalrak Tekrar rESULT DONUYOR
                searchResponseDto.results.map { searchedBookDto ->
                    searchedBookDto.toBook()
                }
                // bu kısım  booka donusturulmus liste dönüyor List<Book>
            // map fonksiyonu ile
            // .searchBooks(query) buradan donen Result nesnesinin datası  List<Book> setleniyor
            // ve Donus olarak Result<List<Book>> doonuyor egerkı .searchBooks(query) Succes ise
            }
    }
}

