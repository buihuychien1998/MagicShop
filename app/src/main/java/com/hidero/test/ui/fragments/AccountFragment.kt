package com.hidero.test.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_account
    }

    override fun initView(view: View) {
        btnSetting.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_settingFragment)
        }
    }
}
