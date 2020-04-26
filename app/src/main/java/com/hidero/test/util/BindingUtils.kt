package com.hidero.test.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hidero.test.R

private val shapeTransform =
    ShapeAppearanceTransformation(R.style.ShapeAppearance_Owl_SmallComponent)

@BindingAdapter("imageUrl")
fun ImageView.loadUrl(url: String?) {
    Glide.with(this.context).load(url).placeholder(R.drawable.ic_no_image)
        .error(R.drawable.ic_error).into(this)
}

@BindingAdapter("photoUrl")
fun ImageView.loadPhotoUrl(url: String?) {
    Glide.with(this.context).load(url).placeholder(R.drawable.ic_user_profile)
        .transform(shapeTransform)
        .error(R.drawable.ic_user_profile).into(this)
}

@BindingAdapter("srcUrl", "circleCrop", "placeholder", "loadListener", requireAll = false)
fun ImageView.bindSrcUrl(
    url: String?,
    circleCrop: Boolean = false,
    placeholder: Drawable?,
    loadListener: GlideDrawableLoadListener?
) {
    val request = Glide.with(this).load(url)
    if (circleCrop) request.circleCrop()
    if (placeholder != null) request.placeholder(placeholder)
    else request.placeholder(R.drawable.ic_no_image)
    if (loadListener != null) request.listener(loadListener)
        .error(R.drawable.ic_error).into(this)
}