package com.hidero.test.util

import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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

@BindingAdapter("adapter")
fun RecyclerView.bindAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    this.adapter = adapter
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

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("renderHtml")
fun bindRenderHtml(view: TextView, description: String?) {
    if (description != null) {
        view.text = HtmlCompat.fromHtml(description, FROM_HTML_MODE_COMPACT)
        view.movementMethod = LinkMovementMethod.getInstance()
    } else {
        view.text = ""
    }
}

@BindingAdapter("wateringText")
fun bindWateringText(textView: TextView, wateringInterval: Int) {
    val resources = textView.context.resources
    val quantityString = resources.getQuantityString(
        R.plurals.watering_needs_suffix,
        wateringInterval, wateringInterval
    )

    textView.text = quantityString
}