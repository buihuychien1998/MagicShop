package com.hidero.test.ui.fragments


import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.adapters.CartAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.CartViewModel
import kotlinx.android.synthetic.main.fragment_cart.*

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : BaseFragment() {
    private lateinit var viewModel: CartViewModel
    private var cartAdapter: CartAdapter? = null
    override fun getLayoutId() = R.layout.fragment_cart


    override fun initViews(view: View) {
        btnOrder.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }
        btnTrash.setOnClickListener {

        }
        cartAdapter = CartAdapter()
        rvCart.adapter = cartAdapter
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.fetchCart("admin")
        viewModel.cart.observe(viewLifecycleOwner, Observer {
            cartAdapter?.submitList(it)
        })
    }


}
