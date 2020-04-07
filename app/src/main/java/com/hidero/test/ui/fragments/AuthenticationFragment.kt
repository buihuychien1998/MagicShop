package com.hidero.test.ui.fragments

import android.os.Build
import android.view.View
import com.hidero.test.R
import com.hidero.test.databinding.FragmentAuthenticationBinding
import com.hidero.test.ui.adapters.AuthenticationPagerAdapter
import com.hidero.test.ui.base.BaseFragment


class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>() {
    override fun getLayoutId() = R.layout.fragment_authentication

    override fun initViews(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val pagerAdapter =
            AuthenticationPagerAdapter(childFragmentManager).apply {
                addFragment(LoginFragment())
                addFragment(RegisterFragment())
            }

        binding.viewPager.adapter = pagerAdapter
    }

//     fun onBackPressed() {
//        super.onBackPressed()
//        val returnIntent = Intent()
//        setResult(Activity.RESULT_CANCELED, returnIntent)
//        finish()
//    }
}

