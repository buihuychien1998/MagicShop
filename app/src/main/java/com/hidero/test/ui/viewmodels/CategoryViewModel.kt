package com.hidero.test.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.CategoryRepository
import com.hidero.test.data.repository.remote.CoroutineRepository
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.DELAY_LOAD
import com.hidero.test.util.baseUrl
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class CategoryViewModel : BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getCoroutineData(baseUrl)
    }
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository =
        CoroutineRepository(apiService)
    private val categoryRepository =
        CategoryRepository()

    private val _genres = MutableLiveData<MutableList<String?>>()
    val genre: LiveData<MutableList<String?>>
        get() = _genres

    private val _authors = MutableLiveData<MutableList<String?>>()
    val author: LiveData<MutableList<String?>>
        get() = _authors

    val genreId = MutableLiveData<Int>()
    val authorId = MutableLiveData<Int>()
    val books: LiveData<PagedList<Book>> by lazy {
        Transformations.switchMap(genreId) { data1 ->
            Transformations.switchMap(authorId) { data2 ->
                categoryRepository.filter(data1, data2)
            }
        }
    }
    val networkState: LiveData<NetworkState> by lazy {
        categoryRepository.getNetworkState()
    }

    val currentPos = MutableLiveData<Int?>()

    fun fetchGenre() {
        scope.launch {
            try {

                val tmp: MutableList<String?> = ArrayList()
                tmp.add("Tất cả")
                delay(DELAY_LOAD)
                repository.getGenre()?.forEach {
                    tmp.add(it.genreName)
                }
                _genres.postValue(tmp)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }

    fun fetchAuthor() {
        scope.launch {
            try {
                val tmp: MutableList<String?> = ArrayList()
                tmp.add("Tất cả")
                delay(DELAY_LOAD)
                repository.getAuthor()?.forEach {
                    tmp.add(it.authorName)
                }
                _authors.postValue(tmp)
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }
    }


    fun listIsEmpty() = categoryRepository.listIsEmpty()

    fun retry() = categoryRepository.retry()

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
        categoryRepository.cancel()
    }

}