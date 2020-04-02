package com.hidero.test.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.pageSize
import io.reactivex.disposables.CompositeDisposable


class BookRepository(private val apiService: APIService) {
    private lateinit var bookPagedList: LiveData<PagedList<Book>>
    private lateinit var bookDataSourceFactory: BookDataSourceFactory
    private val config by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()
    }

    fun fetchLiveBookPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Book>> {
        bookDataSourceFactory = BookDataSourceFactory(apiService, compositeDisposable)
        bookPagedList = LivePagedListBuilder(bookDataSourceFactory, config).build()
        return bookPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<BookDataSource,
            NetworkState>(bookDataSourceFactory.booksLiveDataSource, BookDataSource::networkState)


    fun retry() {
        bookDataSourceFactory.booksLiveDataSource.value?.retry()
    }


}