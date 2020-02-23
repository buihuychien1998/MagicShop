package com.hidero.test.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_setting
    }

    override fun initView(view: View) {
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
