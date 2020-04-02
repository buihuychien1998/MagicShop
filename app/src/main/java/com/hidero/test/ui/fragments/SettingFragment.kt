package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun initViews(view: View) {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnInfoAccount.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }

    }

}
