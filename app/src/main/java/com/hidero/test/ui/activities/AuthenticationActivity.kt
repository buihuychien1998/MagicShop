package com.hidero.test.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.hidero.test.R
import com.hidero.test.ui.adapters.AuthenticationPagerAdapter
import com.hidero.test.ui.base.BaseActivity
import com.hidero.test.ui.fragments.LoginFragment
import com.hidero.test.ui.fragments.RegisterFragment
import kotlinx.android.synthetic.main.activity_authentication.*


class AuthenticationActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_authentication

    override fun initViews(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val pagerAdapter =
            AuthenticationPagerAdapter(supportFragmentManager)

        pagerAdapter.addFragment(LoginFragment())
        pagerAdapter.addFragment(RegisterFragment())
        viewPager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }
}

