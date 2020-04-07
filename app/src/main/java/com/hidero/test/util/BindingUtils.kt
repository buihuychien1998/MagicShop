package com.hidero.test.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hidero.test.R

@BindingAdapter("imageUrl")
fun ImageView.loadUrl(url: String?) {
    Glide.with(this.context).load(url).placeholder(R.drawable.ic_no_image)
        .error(R.drawable.ic_error).into(this)
}