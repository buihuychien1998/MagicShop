package com.hidero.test.ui.fragments

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hidero.test.R
import com.hidero.test.databinding.FragmentDemoObjectBinding
import com.hidero.test.ui.adapters.CartAdapter
import com.hidero.test.ui.adapters.CheckoutAdapter
import com.hidero.test.ui.adapters.DemoCollectionAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.BillViewModel
import com.hidero.test.util.ARG_OBJECT

class DemoObjectFragment : BaseFragment<FragmentDemoObjectBinding>() {
    override fun getLayoutId() = R.layout.fragment_demo_object
    private lateinit var viewModel: BillViewModel
    private lateinit var billAdapter: CheckoutAdapter

    override fun initViews(view: View) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            getInt(ARG_OBJECT).toString()
        }
        viewModel = ViewModelProvider(this)[BillViewModel::class.java]
        viewModel.fetchBill(viewModel.account.value?.username)
        billAdapter = CheckoutAdapter()
        binding.rvBill.adapter = billAdapter
        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.cart.observe(viewLifecycleOwner, Observer {
            billAdapter.submitList(it)
        })
    }

}