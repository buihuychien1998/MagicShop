package com.hidero.test.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hidero.test.data.repository.local.FavouriteBookRepository
import com.hidero.test.data.valueobject.FavouriteBook
import com.hidero.test.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FavouriteViewModel(application: Application) : BaseViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)
    private var repository: FavouriteBookRepository = FavouriteBookRepository(application)
    fun select(username: String?) = repository.allBooks(username)

    fun insert(book: FavouriteBook?) {
        scope.launch{
            repository.insert(book)
        }
    }
    fun delete(bookId: Int?, username: String?){
        scope.launch {
            repository.delete(bookId, username)
        }
    }
}