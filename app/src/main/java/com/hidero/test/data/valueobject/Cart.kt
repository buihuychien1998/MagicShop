package com.hidero.test.data.valueobject

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cart {
    @SerializedName("cartId")
    @Expose
    var cartId: Int? = null
    @SerializedName("bookId")
    @Expose
    var bookId: Int? = null
    @SerializedName("bookName")
    @Expose
    var bookName: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Int? = null
    @SerializedName("cost")
    @Expose
    var cost: Int? = null
    @SerializedName("bookImage")
    @Expose
    var bookImage: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("maxq")
    @Expose
    var maxq: Int? = null
}

class DiffUtilCallBack : DiffUtil.ItemCallback<Cart>() {
    override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem.cartId == newItem.cartId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
        return oldItem == newItem
    }

}