package com.hidero.test.data.valueobject

import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table", primaryKeys = ["bookId", "username"])
data class FavouriteBook(
    @ColumnInfo(name = "bookId")
    @NonNull
    val bookId: Int,
    @ColumnInfo(name = "bookName")
    val bookName: String?,
    @ColumnInfo(name = "bookImage")
    val bookImage: String?,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "username")
    @NonNull
    val username: String
)

class FavouriteCallBack : DiffUtil.ItemCallback<FavouriteBook>() {
    override fun areItemsTheSame(oldItem: FavouriteBook, newItem: FavouriteBook): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FavouriteBook, newItem: FavouriteBook): Boolean {
        return oldItem.bookId == newItem.bookId
    }

}