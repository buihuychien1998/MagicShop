package com.hidero.test.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hidero.test.R
import com.hidero.test.data.valueobject.Cart
import com.hidero.test.data.valueobject.DiffUtilCallBack
import com.hidero.test.databinding.ItemCartBinding
import com.hidero.test.ui.viewmodels.CartViewModel
import com.hidero.test.util.disableClickTemporarily

class CartAdapter(private val viewModel: CartViewModel) :
    ListAdapter<Cart, RecyclerView.ViewHolder>(DiffUtilCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CartViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_cart,
                parent, false
            )
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as CartViewHolder).bind(getItem(position))


    override fun submitList(list: MutableList<Cart>?) =
        super.submitList(list?.let { ArrayList(it) })


    fun getItemPos(position: Int): Cart? = getItem(position)

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart) {
            binding.run {
                handlers = this@CartViewHolder
                data = cart
                executePendingBindings()
            }
        }

        fun onClick(view: View, cart: Cart) {
            val username = viewModel.account.value?.username
            view.disableClickTemporarily()
            when (view.id) {
                R.id.btnMinus -> {
                    var number = requireNotNull(cart.quantity)
                    number--
                    with(cart) {
                        quantity = number
                        viewModel.updateCart(username, bookId, quantity, cost)
                    }
                    notifyItemChanged(adapterPosition)
                }
                R.id.btnPlus -> {
                    var number = requireNotNull(cart.quantity)
                    number++
                    with(cart) {
                        quantity = number
                        viewModel.updateCart(username, bookId, quantity, cost)
                    }
                    notifyItemChanged(adapterPosition)
                }

            }
        }
    }

    fun removeItem(position: Int) {
        viewModel.apply {
            deleteCart(
                account.value?.username,
                getItem(position)?.bookId
            )
        }
    }

}

