package com.hidero.test.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hidero.test.R
import com.hidero.test.data.valueobject.FavouriteBook
import com.hidero.test.data.valueobject.FavouriteCallBack
import com.hidero.test.databinding.ItemFavouriteBinding
import com.hidero.test.ui.viewmodels.FavouriteViewModel

class FavoriteAdapter(private val viewModel: FavouriteViewModel?) : ListAdapter<FavouriteBook, RecyclerView.ViewHolder>(
    FavouriteCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView =
            DataBindingUtil.inflate<ItemFavouriteBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_favourite,
                parent,
                false
            )
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: FavouriteBook) {
            binding.run {
                handlers = this@ViewHolder
                data = book
                executePendingBindings()
            }
        }

        fun onClick(view: View, book: FavouriteBook) {
            when (view.id) {
                R.id.btnDelete -> {
                    viewModel?.delete(book.bookId, viewModel.account.value?.username)
                }
            }
        }
    }


}