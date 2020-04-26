package com.hidero.test.data.valueobject

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Book(
    var bookId: Int,
    var bookName: String?,
    var quantity: Int,
    var cost: Int,
    var bookImage: String?,
    var description: String?,
    var languageId: Int?,
    @SerializedName("authorId")
    @Expose
    var author: String,
    var genreId: Int?
)

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.bookId == newItem.bookId
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

}