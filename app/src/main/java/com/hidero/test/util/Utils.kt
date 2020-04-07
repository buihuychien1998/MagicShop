package com.hidero.test.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
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

fun Context.showDialog(message: String) =
    SpotsDialog.Builder()
        .setContext(this)
        .setMessage(message)
        .setCancelable(true)
        .build()



fun delayFunction(function: () -> Unit, delay: Long) {
    Handler().postDelayed(function, delay)
}

fun Context.getBitmap(drawableId: Int): Bitmap? =
    BitmapFactory.decodeResource(resources, drawableId)

fun View.disableClickTemporarily(){
    isClickable = false
    postDelayed({
        isClickable = true
    },1000)
}