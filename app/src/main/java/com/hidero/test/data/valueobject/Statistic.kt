package com.hidero.test.data.valueobject

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Statistic(
    @SerializedName("thang")
    val month: Int,
    @SerializedName("tt")
    val money: Float
): Parcelable