package com.hidero.test.data.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hidero.test.R
import com.hidero.test.data.valueobject.FavouriteBook

@Database(entities = [FavouriteBook::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {

        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getDatabase(context: Context): BookDatabase? {
            if (INSTANCE == null) {
                synchronized(BookDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BookDatabase::class.java, context.getString(R.string.book_database)
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}