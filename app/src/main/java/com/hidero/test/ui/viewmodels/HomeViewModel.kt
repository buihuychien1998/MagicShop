package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.BookRepository
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.baseUrl
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel : BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getRxData(baseUrl)
    }
    private val compositeDisposable = CompositeDisposable()
    private val repository =
        BookRepository(apiService)
    val bookList: LiveData<PagedList<Book>> by lazy {
        repository.fetchBook(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun retry() = repository.retry()

    fun listIsEmpty() = repository.listIsEmpty()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}