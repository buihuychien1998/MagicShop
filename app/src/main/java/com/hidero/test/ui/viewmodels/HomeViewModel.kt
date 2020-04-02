package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.BookRepository
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.baseUrl
import io.reactivex.disposables.CompositeDisposable

class HomeFragmentViewModel : ViewModel() {
    private val apiService by lazy {
        APIUtil.getData(baseUrl)
    }
    private val bookRepository: BookRepository by lazy {
        BookRepository(apiService)
    }
    private val compositeDisposable by lazy { CompositeDisposable() }

    val bookList: LiveData<PagedList<Book>> by lazy {
        bookRepository.fetchLiveBookPagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        bookRepository.getNetworkState()
    }

    fun retry() {
        bookRepository.retry()
    }

    fun listIsEmpty(): Boolean {
        return bookList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}