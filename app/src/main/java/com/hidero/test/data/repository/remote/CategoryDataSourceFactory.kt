package com.hidero.test.data.repository.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hidero.test.data.repository.remote.CategoryDataSource
import com.hidero.test.data.valueobject.Book

class CategoryDataSourceFactory constructor(
    private val genreId: Int,
    private val authorId: Int
) :
    DataSource.Factory<Int, Book>() {
    val dataSource = MutableLiveData<CategoryDataSource>()

    override fun create(): DataSource<Int, Book> {
        val bookDataSource =
            CategoryDataSource(
                genreId,
                authorId
            )
        dataSource.postValue(bookDataSource)
        return bookDataSource
    }
}