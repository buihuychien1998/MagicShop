package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.SearchRepository
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.baseUrl
import io.reactivex.disposables.CompositeDisposable

class SearchFragmentViewModel : ViewModel() {
    val keyword = MutableLiveData<String>()
    private val apiService by lazy {
        APIUtil.getData(baseUrl)
    }
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val repository: SearchRepository by lazy { SearchRepository(apiService, compositeDisposable) }
    val bookList: LiveData<PagedList<Book>> by lazy {
        Transformations.switchMap(keyword){
            repository.search(it)
        }
    }

    val networkState: LiveData<NetworkState> by lazy { repository.getNetworkState() }

    fun retry() = repository.retry()

    fun listIsEmpty() = bookList.value?.isEmpty() ?: true
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}