package com.hidero.test.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.hidero.test.R
import com.hidero.test.widget.DialogProgressBar


class DialogUtils private constructor() {

    private var mProgressDialog: DialogProgressBar? = null

    fun showLoadingDialog(context: Context, message: Int) {
        mProgressDialog = DialogProgressBar(context)
        mProgressDialog!!.startLoading()
    }

    fun showLoadingDialog(context: Context, message: String) {
        mProgressDialog = DialogProgressBar(context)
        mProgressDialog!!.startLoading()
    }

    fun dismissLoadingDialog() {
        if (mProgressDialog != null) {
            try {
                mProgressDialog!!.loadingDissmiss()
            } catch (ex: IllegalArgumentException) {
                Log.e(
                    javaClass.simpleName,
                    "Activity is already finished, no need dismissing dialog"
                )
            }

        }
    }

    companion object {

        private var instance: DialogUtils = DialogUtils()

        fun getInstance(): DialogUtils {
            return instance;
        }

        fun showAlert(
            context: Context,
            title: Int,
            message: Int,
            onOKClickListener: DialogCallback
        ) {
            buildAlertWithCancel(context, title, message, R.string.ok, R.string.cancel,onOKClickListener, true)
        }

        fun showAlert(
            context: Context,
            title: String,
            message: String,
            onOKClickListener: DialogCallback
        ) {
            buildAlert(
                context,
                title,
                message,
                context.getString(R.string.ok),
                onOKClickListener,
                false
            )
                .show()
        }
        fun showAlertWithCancel(
            context: Context,
            title: String?,
            message: String,
            onOKClickListener: DialogCallback
        ) {
            buildAlertWithCancel(
                context,
                title,
                message,
                "はい",
                "キャンセル",
                onOKClickListener,
                true
            )
                .show()
        }
        private fun buildAlertWithCancel(
            context: Context,
            title: Int,
            message: Int,
            positiveText: Int,
            negativeText: Int,
            onOKClickListener: DialogCallback,
            cancelable: Boolean
        ) {
            buildAlertWithCancel(
                context,
                if (title == -1) null else context.getString(title),
                context.getString(message),
                context.getString(positiveText),
                context.getString(negativeText),
                onOKClickListener,
                cancelable
            )
        }

        fun buildAlert(
            context: Context,
            title: String?,
            message: String,
            buttonText: String,
            onOKClickListener: DialogCallback,
            cancelable: Boolean
        ): MaterialDialog {
            return MaterialDialog(context).show {
                message(null, message)
                positiveButton(null, buttonText, click = onOKClickListener)
//                negativeButton(null, "Cancel", null)
                if (!TextUtils.isEmpty(title)) {
                    title(text = title)
                }
                cancelable(cancelable)
            }
        }

        private fun buildAlertWithCancel(
            context: Context,
            title: String?,
            message: String,
            positiveText: String,
            negativeText: String,
            onOKClickListener: DialogCallback,
            cancelable: Boolean
        ): MaterialDialog {
            return MaterialDialog(context).show {
                message(null, message)
                positiveButton(null, positiveText, click = onOKClickListener)
                negativeButton(null, negativeText, null)
                if (!TextUtils.isEmpty(title)) {
                    title(text = title)
                }
                cancelable(cancelable)
            }
        }

    }
}
