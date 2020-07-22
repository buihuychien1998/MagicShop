package com.hidero.test.ui.fragments

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.hidero.test.R
import com.hidero.test.databinding.FragmentSupportBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.SupportViewModel
import com.hidero.test.util.DialogUtils
import com.hidero.test.util.observeOnce


class SupportFragment : BaseFragment<FragmentSupportBinding>() {
    override fun getLayoutId() = R.layout.fragment_support
    private lateinit var viewModel: SupportViewModel
    override fun initViews(view: View) {
        viewModel = ViewModelProvider(this)[SupportViewModel::class.java]
        binding.tvSales.setOnClickListener {
            findNavController().navigate(R.id.action_supportFragment_to_statisticFragment)
        }
        binding.tvNumberOrders.setOnClickListener {
            viewModel.getNumberBills()
            val obs = Observer<String> {
                DialogUtils.showAlert(
                    requireContext(),
                    "Thống kê",
                    "Số lượng đơn đặt hàng: $it",
                    object : DialogCallback {
                        override fun invoke(p1: MaterialDialog) {
                            p1.dismiss()
                        }
                    })
            }
            viewModel.numberBill.observeOnce(viewLifecycleOwner, obs)
        }

        binding.tvNumberBills.setOnClickListener {
            DialogUtils.showAlert(
                requireContext(),
                "Thống kê",
                "Số lượng đơn hoàn tất: 0",
                object : DialogCallback {
                    override fun invoke(p1: MaterialDialog) {
                        p1.dismiss()
                    }
                })
        }
        binding.ibUp.setOnClickListener {
            var y = binding.npYear.text.toString().toInt()
            binding.npYear.setText((++y).toString())
        }
        binding.ibDown.setOnClickListener {
            var y = binding.npYear.text.toString().toInt()
            binding.npYear.setText((--y).toString())
        }

        binding.tvUpdateBills.setOnClickListener {
            findNavController().navigate(SupportFragmentDirections.actionSupportFragmentToUpdateBillFragment())
        }
    }

    private fun subscribeUi() {


    }


}