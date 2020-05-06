package com.hidero.test.ui.dialogs


import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Button
import com.hidero.test.R


class InternetDialog(context: Context): Dialog(context, R.style.df_dialog) {
    fun showDialog() {
        setContentView(R.layout.dialog_no_internet)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        findViewById<Button>(R.id.btnSpinAndWinRedeem).setOnClickListener {
            dismiss()
        }
        show()
    }

}