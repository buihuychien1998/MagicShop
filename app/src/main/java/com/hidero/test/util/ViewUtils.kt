package com.hidero.test.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.view.WindowManager
import androidx.core.content.ContextCompat
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.google.android.material.textfield.TextInputLayout
import com.hidero.test.R
import com.hidero.test.data.valueobject.SliderItem
import com.hidero.test.ui.adapters.SliderAdapterExample

val isNotEmpty = { til: TextInputLayout ->
    val edt = til.editText
    if (TextUtils.isEmpty(edt?.text.toString())) {
        edt?.requestFocus()
        til.error = "Thông tin này không thể bỏ trống"
        false
    } else {
        til.isErrorEnabled = false
        true
    }
}

fun CircularProgressButton.morphAndRevert(context: Context, revertTime: Long = 1500) {
    doneLoadingAnimation(
        ContextCompat.getColor(
            context,
            R.color.colorPrimary
        ), context.getBitmap(R.drawable.ic_done_white_48dp)
    )
    Handler().postDelayed(::stopAnimation, revertTime)
    Handler().postDelayed(::revertAnimation, revertTime)
}

fun Activity.changeStatusBarColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //            window.setStatusBarColor(Color.TRANSPARENT)
        window.statusBarColor = ContextCompat.getColor(
            applicationContext,
            android.R.color.transparent
        )
    }
}

fun renewItems(adapter: SliderAdapterExample) {
    ArrayList<SliderItem>().also {
        //dummy data
        for (i in 0..4) {
            SliderItem().apply {
                when {
                    i % 5 == 0 -> {
                        imageUrl =
                            "https://dongho24h.com/upload/1/articles/l_770674346_Untitled-7.jpg"
                    }
                    i % 5 == 1 -> {
                        imageUrl =
                            "https://canthoplus.com/wp-content/uploads/2018/12/giay-ha-anh-can-tho-banner.jpg"
                    }
                    i % 5 == 2 -> {
                        imageUrl =
                            "https://khuyenmaiviet.vn/wp-content/uploads/2019/11/76793129_2285972111511566_282390569248882688_o.jpg"
                    }
                    i % 5 == 3 -> {
                        imageUrl =
                            "https://hoaanhdao.vn/2019_img/files/san-pham/640x340-mobile%20(1).png"
                    }
                    else -> {
                        imageUrl =
                            "https://img.kam.vn/images/414x0/ed264b973dda4ee5b852f77e93f724b1/dong-ho-duy-anh-dong-ho-nhat-sale-off-15.jpg"
                    }
                }
                description = imageUrl
                it.add(this)
            }

        }
        adapter.renewItems(it)
    }

}