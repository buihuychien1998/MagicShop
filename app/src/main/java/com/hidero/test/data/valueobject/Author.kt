package com.hidero.test.data.valueobject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Author: Serializable{
    @SerializedName("idAuthor")
    @Expose
    val authorId: String? = null
    @SerializedName("nameAuthor")
    @Expose
    val authorName: String? = null


}