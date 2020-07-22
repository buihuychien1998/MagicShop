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
import com.hidero.test.data.valueobject.Rating
import com.hidero.test.data.valueobject.RatingCallBack
import com.hidero.test.databinding.ItemCommentBinding
import com.hidero.test.databinding.ItemFavouriteBinding
import com.hidero.test.ui.viewmodels.FavouriteViewModel

class CommentAdapter() : ListAdapter<Rating, RecyclerView.ViewHolder>(
    RatingCallBack()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView =
            DataBindingUtil.inflate<ItemCommentBinding>(
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

    inner class ViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rating: Rating) {
            binding.run {
                data = rating
                executePendingBindings()
            }
        }
    }


}