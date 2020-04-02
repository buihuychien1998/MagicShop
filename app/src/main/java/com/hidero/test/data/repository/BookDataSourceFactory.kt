package com.hidero.test.data.reponsitory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hidero.test.data.api.APIService
import com.hidero.test.data.valueobject.Book
import io.reactivex.disposables.CompositeDisposable

class BookDataSourceFactory(
    private val apiService: APIService,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Book>() {

    val booksLiveDataSource = MutableLiveData<BookDataSource>()

    override fun create(): DataSource<Int, Book> {
        val bookDataSource = BookDataSource(apiService, compositeDisposable)
        booksLiveDataSource.postValue(bookDataSource)
        return bookDataSource
    }
}