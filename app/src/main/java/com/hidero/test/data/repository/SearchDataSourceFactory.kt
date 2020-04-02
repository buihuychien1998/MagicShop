package com.hidero.test.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Book
import io.reactivex.disposables.CompositeDisposable

class SearchDataSourceFactory(
    private val apiService: APIService,
    private val compositeDisposable: CompositeDisposable,
    private val keyword: String
) : DataSource.Factory<Int, Book>() {

    val booksLiveDataSource = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, Book> {
        val searchDataSource = SearchDataSource(apiService, compositeDisposable, keyword)
        booksLiveDataSource.postValue(searchDataSource)
        return searchDataSource
    }
}