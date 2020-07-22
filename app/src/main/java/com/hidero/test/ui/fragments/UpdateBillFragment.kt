package com.hidero.test.ui.fragments

import android.view.View
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.databinding.FragmentUpdateBillBinding
import com.hidero.test.ui.base.BaseFragment

class UpdateBillFragment : BaseFragment<FragmentUpdateBillBinding>() {
    override fun getLayoutId() = R.layout.fragment_update_bill

    override fun initViews(view: View) {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

    }

}