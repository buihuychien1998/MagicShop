package com.hidero.test.data.repository.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hidero.test.data.api.APIService
import com.hidero.test.data.repository.remote.BookDataSource
import com.hidero.test.data.repository.remote.BookDataSourceFactory
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.pageSize
import io.reactivex.disposables.CompositeDisposable


class BookRepository(
    private val apiService: APIService
) {
    private lateinit var bookDataSourceFactory: BookDataSourceFactory

    private lateinit var bookPagedList: LiveData<PagedList<Book>>
    fun fetchBook(compositeDisposable: CompositeDisposable): LiveData<PagedList<Book>> {
        bookDataSourceFactory =
            BookDataSourceFactory(
                apiService,
                compositeDisposable
            )
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(pageSize)
                .build()

        bookPagedList = LivePagedListBuilder(bookDataSourceFactory, config).build()
        return bookPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<BookDataSource,
            NetworkState>(bookDataSourceFactory.booksLiveDataSource, BookDataSource::networkState)


    fun retry() =
        bookDataSourceFactory.booksLiveDataSource.value?.retry()

    fun listIsEmpty() = bookPagedList.value?.isEmpty() ?: true

}