package com.hidero.book.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Species: Serializable {
    @SerializedName("idSpecies")
    @Expose
    val idSpecies: String? = null
    @SerializedName("nameSpecies")
    @Expose
    val nameSpecies: String? = null


}