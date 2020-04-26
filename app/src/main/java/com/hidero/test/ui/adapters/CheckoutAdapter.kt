package com.hidero.test.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hidero.test.R
import com.hidero.test.data.valueobject.Cart
import com.hidero.test.data.valueobject.DiffUtilCallBack
import com.hidero.test.databinding.ItemCheckoutBinding

class CheckoutAdapter : ListAdapter<Cart, RecyclerView.ViewHolder>(DiffUtilCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CheckoutViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_checkout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as CheckoutViewHolder).bind(getItem(position))


    override fun submitList(list: MutableList<Cart>?) =
        super.submitList(list?.let { ArrayList(it) })


    class CheckoutViewHolder(private val binding: ItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart) {
            binding.run {
                data = cart
                executePendingBindings()
            }
        }

    }

}