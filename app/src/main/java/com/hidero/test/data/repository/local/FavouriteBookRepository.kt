package com.hidero.test.data.repository.local

import android.app.Application
import com.hidero.test.data.valueobject.FavouriteBook

class FavouriteBookRepository(application: Application) {

    private val bookDao: BookDao

    init {
        BookDatabase.getDatabase(application).let {
            bookDao = it!!.bookDao()
        }
    }

    val allBooks = { username: String? ->
        bookDao.getBooks(username) }

    suspend fun insert(book: FavouriteBook?) = bookDao.insertBook(book)

    suspend fun delete(bookId: Int?, username: String?) = bookDao.delete(bookId, username)

}