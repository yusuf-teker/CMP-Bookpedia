package com.plcoding.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.plcoding.bookpedia.book.data.database.FavoriteBookDao
import com.plcoding.bookpedia.book.data.database.FavoriteBookDatabase
import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.mappers.toBookEntity
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    // Implementation değişebilir ileride fakat bu interface değişmez o yüzden
    //implementation değişse ve farklı bir data source yazılsa bile burayı değiştirmeye gerek kalmaz
    // ve bu durumda domain layerııda değiştirmeye gerek kalmaz

    private val localBookDataSource: FavoriteBookDao
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

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError.Remote> {

        val localBook = localBookDataSource.getFavoriteBookById(bookId)
         return if (localBook != null){
             Result.Success(localBook.description,)
         }else{
             remoteBookDataSource.getBookDetails(bookId)
                 .map { bookWorkDto ->
                     bookWorkDto.description
                 }
         }



    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return localBookDataSource.getFavoriteBooks()
            .map {
                it.map { bookEntity ->
                    bookEntity.toBook()
                }
            }
    }

    override fun isBookFavorite(bookId: String): Flow<Boolean> {
        return localBookDataSource.getFavoriteBooks()
            .map {
                it.any { bookEntity ->
                    bookEntity.id == bookId
                }
            }
    }

    override suspend fun markAsFavorite(book: Book): Result<Unit, DataError> {
        return try {
            localBookDataSource.upsert(book.toBookEntity())
            Result.Success(Unit)
        }catch (e: SQLiteException){
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFavoriteBook(bookId: String) {
         localBookDataSource.deleteFavoriteBookById(bookId)
    }


}

