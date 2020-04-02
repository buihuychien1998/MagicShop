package com.hidero.test.ui.fragments

import android.os.Build
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.adapters.AuthenticationPagerAdapter
import com.hidero.test.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_authentication.*

class AuthenticationFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_authentication
    }

    override fun initViews(view: View) {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val pagerAdapter =
            AuthenticationPagerAdapter(requireActivity().supportFragmentManager)

        pagerAdapter.addFragment(LoginFragment())
        pagerAdapter.addFragment(RegisterFragment())
        viewPager.adapter = pagerAdapter
    }

    private fun onBackPress(){
        findNavController().navigateUp()
    }
}

