package com.hidero.test.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hidero.test.data.valueobject.Book

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Book>()

    fun select(book: Book) {
        selected.value = book
    }
}