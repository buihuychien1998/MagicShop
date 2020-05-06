package com.hidero.test.data.valueobject

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Author : Serializable {

    @SerializedName("authorId")
    @Expose
    val authorId: String? = null

    @SerializedName("authorName")
    @Expose
    val authorName: String? = null


}