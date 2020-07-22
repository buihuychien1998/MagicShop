package com.hidero.test.ui.fragments

import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.hidero.test.R
import com.hidero.test.databinding.FragmentBillBinding
import com.hidero.test.ui.adapters.DemoCollectionAdapter
import com.hidero.test.ui.base.BaseFragment


class BillFragment : BaseFragment<FragmentBillBinding>() {
    //safe arg
//    val arg: BillFragmentArgs by navArgs()
    override fun getLayoutId() = R.layout.fragment_bill
    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    override fun initViews(view: View) {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setTitle("Đơn hàng")
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        demoCollectionAdapter = DemoCollectionAdapter(this)

        binding.viewpager.adapter = demoCollectionAdapter
        arguments?.let {
//            arg.pos
            binding.viewpager.postDelayed({ binding.viewpager.currentItem = it.getInt("pos") }, 100)
        }

        TabLayoutMediator(binding.tabs, binding.viewpager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0->{
                        tab.text = "Chờ thanh toán"
                    }
                    1->{
                        tab.text = "Chờ vận chuyển"
                    }
                    2->{
                        tab.text = "Chờ giao hàng"
                    }
                    3->{
                        tab.text = "Chưa đánh giá"
                    }

                }

            }).attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }
}