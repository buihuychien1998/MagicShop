package com.hidero.test.data.valueobject

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class Rating {
    @SerializedName("madg")
    @Expose
    var ratingId: Int? = 0
    @SerializedName("tentk")
    @Expose
    var username: String? = ""
    @SerializedName("masach")
    @Expose
    var bookId: Int? = 0
    @SerializedName("rating")
    @Expose
    var rating: Float? = 0f
    @SerializedName("chude")
    @Expose
    var title: String? = ""
    @SerializedName("noidung")
    @Expose
    var content: String? = ""
    @SerializedName("ngaydg")
    @Expose
    var cd: String? = SimpleDateFormat(
        "dd-MMM-yyyy hh:mm:ss aa",
        Locale.getDefault()
    ).format(Calendar.getInstance(Locale.getDefault()))
    @SerializedName("hinhanh")
    @Expose
    var userPhoto: String? = ""
}

class RatingCallBack : DiffUtil.ItemCallback<Rating>() {
    override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem.ratingId == newItem.ratingId
    }

}