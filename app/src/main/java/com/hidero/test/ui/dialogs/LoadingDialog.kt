package com.hidero.test.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.hidero.test.R


class LoadingDialog(context: Context) : Dialog(context) {

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}