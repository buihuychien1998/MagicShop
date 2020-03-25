package com.hidero.test.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hidero.test.R
import com.hidero.test.ui.base.BaseActivity
import com.hidero.test.ui.fragments.LoginFragment
import com.hidero.test.ui.fragments.RegisterFragment
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : BaseActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_account
    }

    override fun initViews(savedInstanceState: Bundle?) {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val pagerAdapter =
            AuthenticationPagerAdapter(supportFragmentManager)

        pagerAdapter.addFragment(LoginFragment())
        pagerAdapter.addFragment(RegisterFragment())
        viewPager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right)
    }

    internal class AuthenticationPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragmentList: ArrayList<Fragment> = ArrayList()
        override fun getItem(i: Int): Fragment {
            return fragmentList[i]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }
    }
}

