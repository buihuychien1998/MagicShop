package com.hidero.test.data.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hidero.test.data.valueobject.FavouriteBook

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: FavouriteBook?)

    @Query("SELECT * from book_table WHERE username = :username")
    fun getBooks(username: String?) : LiveData<List<FavouriteBook>>

    @Query("DELETE FROM book_table WHERE username = :username")
    suspend fun deleteAll(username: String?)

    @Query("DELETE FROM book_table WHERE username = :username AND bookId =:bookId")
    suspend fun delete(bookId: Int?, username: String?)

}