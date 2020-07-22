package com.hidero.test.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import com.hidero.test.R

class DialogProgressBar(context: Context) {
    private val mDialog: Dialog?

    init {
        mDialog = Dialog(context, R.style.LoadingDialog)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_loading_progress)
        mDialog.setCancelable(false)
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window!!.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    fun startLoading() {
        mDialog!!.show()
    }

    fun loadingDissmiss() {
        if (mDialog != null && mDialog.isShowing) {
            mDialog.dismiss()
        }

    }
}
