package com.hidero.test.data.repository.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.FIRST_PAGE
import com.hidero.test.util.baseUrl
import com.hidero.test.util.pageSize
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class CategoryDataSource(
    private val genreId: Int, private val authorId: Int
) :
    PageKeyedDataSource<Int, Book>() {
    private val compositeDisposable = CompositeDisposable()
    private val apiService by lazy {
        APIUtil.getRxData(baseUrl)
    }

    private var retryCompletable: Completable? = null
    private var page = FIRST_PAGE
    val networkState = MutableLiveData<NetworkState>()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Book>
    ) {
        updateState(NetworkState.LOADING)
        compositeDisposable.add(
            callLatestNewsAsync(page)
                .subscribe(
                    { response ->
                        updateState(NetworkState.LOADED)
                        callback.onResult(
                            response,
                            null,
                            page + 1
                        )
                    },
                    {
                        updateState(NetworkState.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Book>) {
        updateState(NetworkState.LOADING)
        compositeDisposable.add(
            callLatestNewsAsync(params.key)
                .subscribe(
                    { response ->
                        if (response.size == 0) {
                            updateState(NetworkState.ENDOFLIST)
                        } else {
                            updateState(NetworkState.LOADED)
                            callback.onResult(
                                response,
                                params.key + 1
                            )
                        }

                    },
                    {
                        updateState(NetworkState.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Book>) {}

    private fun updateState(state: NetworkState) {
        networkState.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    private fun callLatestNewsAsync(pageNumber: Int): Observable<MutableList<Book>> =
        apiService.filter(
            genreId, authorId, pageNumber, pageSize
        )

    fun cancel()=
        compositeDisposable.dispose()


}