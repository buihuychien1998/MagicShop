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


class SearchRepository(
    private val apiService: APIService,
    private val compositeDisposable: CompositeDisposable
) {
    private lateinit var bookPagedList: LiveData<PagedList<Book>>
    private var keyword = ""

    private lateinit var bookDataSourceFactory: SearchDataSourceFactory
    private val config by lazy {
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()
    }

    private fun fetchLiveBookPagedList(): LiveData<PagedList<Book>> {
        bookDataSourceFactory = SearchDataSourceFactory(apiService, compositeDisposable, keyword)
        bookPagedList = LivePagedListBuilder(bookDataSourceFactory, config).build()
        return bookPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<SearchDataSource,
            NetworkState>(bookDataSourceFactory.booksLiveDataSource, SearchDataSource::networkState)


    fun retry() {
        bookDataSourceFactory.booksLiveDataSource.value?.retry()
    }

    fun search(text: String):  LiveData<PagedList<Book>>{
        keyword = text
        return fetchLiveBookPagedList()
    }
}