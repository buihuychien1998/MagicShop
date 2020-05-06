package com.hidero.test.data.valueobject

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class FavouriteBook(
    @PrimaryKey
    @ColumnInfo(name = "bookId")
    val bookId: Int?,
    @ColumnInfo(name = "bookName")
    val bookName: String?,
    @ColumnInfo(name = "bookImage")
    val bookImage: String?,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "username")
    val username: String?
)

class FavouriteCallBack : DiffUtil.ItemCallback<FavouriteBook>() {
    override fun areItemsTheSame(oldItem: FavouriteBook, newItem: FavouriteBook): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FavouriteBook, newItem: FavouriteBook): Boolean {
        return oldItem.bookId == newItem.bookId
    }

}