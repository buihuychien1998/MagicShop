package com.hidero.test.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hidero.test.R
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import kotlinx.android.synthetic.main.item_network_state.view.*
import kotlinx.android.synthetic.main.item_product.view.*


class BookPagedListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<Book, RecyclerView.ViewHolder>(BookDiffCallback()) {

    val DATA_VIEW_TYPE = 1
    private val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            BookItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product, parent, false)
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


    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.bookId == newItem.bookId
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

    }

    inner class BookItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(book: Book?) {
            itemView.tvTitle.text = book?.bookName
            itemView.tvCost.text = "${book?.cost}"
            Glide.with(itemView.context)
                .load(book?.bookImage)
                .apply(
                    RequestOptions().fitCenter()
                        .placeholder(
                            ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.ic_no_image
                            )
                        )
                        .error(ContextCompat.getDrawable(itemView.context, R.drawable.ic_error))
                )
                .into(itemView.ivProduct)

            itemView.setOnClickListener {
                if (onItemClickListener != null) {
                    book?.let { book -> onItemClickListener?.onItemClick(book, adapterPosition) }
                }
            }
        }
    }


    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = VISIBLE
            } else {
                itemView.progress_bar_item.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else {
                itemView.error_msg_item.visibility = View.GONE
            }
        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): NetworkStateItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_network_state, parent, false)
                view.error_msg_item.setOnClickListener { retry() }
                return NetworkStateItemViewHolder(view)
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
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

    interface OnItemClickListener {
        fun onItemClick(book: Book, position: Int)
    }
}