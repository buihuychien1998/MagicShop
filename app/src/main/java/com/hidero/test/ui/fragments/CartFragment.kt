package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_cart.*

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_cart
    }

    override fun initView(view: View) {
        btnOrder.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }
        btnTrash.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_detailProductFragment)
        }
    }


}
