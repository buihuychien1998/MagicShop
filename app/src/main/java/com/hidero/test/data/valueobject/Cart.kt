package com.hidero.book.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Cart {
    @SerializedName("idCart")
    @Expose
    var idCart: Int? = null
    @SerializedName("idBook")
    @Expose
    var idBook: Int? = null
    @SerializedName("nameBook")
    @Expose
    var nameBook: String? = null
    @SerializedName("quantity")
    @Expose
    var quantity: Int? = null
    @SerializedName("cost")
    @Expose
    var cost: Int? = null
    @SerializedName("imageBook")
    @Expose
    var imageBook: String? = null
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