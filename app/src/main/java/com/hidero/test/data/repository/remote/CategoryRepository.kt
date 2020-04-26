package com.hidero.test.data.repository.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.util.pageSize
import timber.log.Timber


class CategoryRepository() {
    private lateinit var dataSourceFactory: CategoryDataSourceFactory

    private lateinit var bookPagedList: LiveData<PagedList<Book>>

    fun filter(genreId: Int, authorId: Int): LiveData<PagedList<Book>> {
        dataSourceFactory =
            CategoryDataSourceFactory(
                genreId,
                authorId
            )
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .build()

        bookPagedList = LivePagedListBuilder(dataSourceFactory, config).build()
        Timber.e(bookPagedList.value?.size.toString())
        return bookPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap(dataSourceFactory.dataSource) {
            it.networkState
        }


    fun listIsEmpty() = bookPagedList.value?.isEmpty() ?: true
    fun retry() = dataSourceFactory.dataSource.value?.retry()
    fun cancel() = dataSourceFactory.dataSource.value?.cancel()

}