package com.hidero.test.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hidero.test.R
import com.hidero.test.data.valueobject.Cart
import com.hidero.test.util.loadUrl
import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter : ListAdapter<Cart, RecyclerView.ViewHolder>(DiffUtilCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cart, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CartViewHolder).bind(getItem(position))
    }

    override fun submitList(list: MutableList<Cart>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cart: Cart) {
            itemView.run {
                tvNameProduct.text = "${cart.bookName}"
                tvQuantity.text = "${cart.quantity}"
                tvCost.text = "${cart.cost}"
                ivProduct.loadUrl(itemView, "${cart.bookImage}")
            }

        }

    }


    class DiffUtilCallBack : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.cartId == newItem.cartId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }

    }
}