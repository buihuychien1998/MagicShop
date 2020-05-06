package com.hidero.test.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hidero.test.R
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.BookDiffCallback
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.databinding.ItemNetworkStateBinding
import com.hidero.test.databinding.ItemProductBinding
import com.hidero.test.util.*


class BookPagedListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<Book, RecyclerView.ViewHolder>(BookDiffCallback()) {

    private var networkState: NetworkState? = null

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            BookItemViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_product,
                    parent,
                    false
                )
            )
        } else {
            NetworkStateItemViewHolder.create(retry, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            (holder as BookItemViewHolder).bind(getItem(position))
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount() =
        super.getItemCount() + if (hasExtraRow()) 1 else 0


    override fun getItemViewType(position: Int) =
        if (hasExtraRow() && position == itemCount - 1) NETWORK_VIEW_TYPE else DATA_VIEW_TYPE


    inner class BookItemViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book?) {
            binding.run {
                data = book
                handlers = this@BookItemViewHolder
                executePendingBindings()
            }
        }

        fun handleEvent() {
            onItemClickListener?.onItemClick(adapterPosition)
        }

    }

    fun getItemPosition(position: Int) =
        super.getItem(position)


    class NetworkStateItemViewHolder(private val binding: ItemNetworkStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: NetworkState?) {
            binding.run {
                if (networkState != null && networkState == NetworkState.LOADING) {
                    progressBar.visibility = VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
                if (networkState != null && networkState == NetworkState.ERROR) {
                    tvError.visibility = VISIBLE
                    tvError.text = networkState.msg
                } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                    tvError.visibility = VISIBLE
                    tvError.text = networkState.msg
                } else {
                    tvError.visibility = View.GONE
                }
                executePendingBindings()
            }

        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): NetworkStateItemViewHolder {
                val binding =
                    DataBindingUtil.inflate<ItemNetworkStateBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.item_network_state,
                        parent,
                        false
                    )
                binding.tvError.setOnClickListener { retry() }
                return NetworkStateItemViewHolder(binding)
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = networkState
        val hadExtraRow = hasExtraRow()
        networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }
    }

}