package com.hidero.test.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.hidero.test.R
import dmax.dialog.SpotsDialog


fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


fun ImageView.loadUrl(view: View, url: String) {
    Glide.with(view)
        .load(url)
        .placeholder(R.drawable.ic_no_image)
        .error(R.drawable.ic_error)
        .fitCenter()
        .into(this)
}

fun Activity.hideKeyBoard() {
    // Check if no view has focus:
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Context.hideKeyBoardFrom(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.showKeyBoard() {
    // Check if no view has focus:
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.showDialog(message: String): AlertDialog =
    SpotsDialog.Builder()
        .setContext(this)
        .setMessage(message)
        .setCancelable(true)
        .build().apply { show() }


fun delayFunction(function: () -> Unit, delay: Long) {
    Handler().postDelayed(function, delay)
}

fun Context.getBitmap(drawableId: Int): Bitmap? =
    BitmapFactory.decodeResource(resources, drawableId)

fun View.disableClickTemporarily() {
    isClickable = false
    postDelayed({
        isClickable = true
    }, 1000)
}

fun Fragment.openImage() {
    val gallery = Intent()
    gallery.type = "image/*"
    gallery.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(Intent.createChooser(gallery, "Chọn ảnh"), IMAGE_REQUEST)
}

fun Activity.openImage() {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(intent, IMAGE_REQUEST)
}

fun Activity.getFileExtension(uri: Uri): String? {
    val contentResolver = contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
}
fun Activity.openGallery() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "image/*"
    startActivityForResult(intent, REQUEST_IMAGE)
}
fun Context?.isAvailable(): Boolean {
    if (this == null) {
        return false
    } else if (this !is Application) {
        if (this is FragmentActivity) {
            return !this.isDestroyed
        } else if (this is Activity) {
            return !this.isDestroyed
        }
    }
    return true
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}