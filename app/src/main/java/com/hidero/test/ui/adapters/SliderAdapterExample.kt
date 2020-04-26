package com.hidero.test.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import com.hidero.test.R
import com.hidero.test.data.valueobject.SliderItem
import com.hidero.test.ui.adapters.SliderAdapterExample.SliderAdapterVH
import com.hidero.test.util.loadUrl
import com.hidero.test.util.showToast
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.*

class SliderAdapterExample(private val context: Context) :
    SliderViewAdapter<SliderAdapterVH>() {
    private var mSliderItems: MutableList<SliderItem>? = ArrayList()

    fun renewItems(sliderItems: MutableList<SliderItem>?) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems!!.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: SliderItem) {
        mSliderItems!!.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider_layout, parent, false)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem = mSliderItems!![position]
        viewHolder.textViewDescription.text = sliderItem.description
        viewHolder.textViewDescription.textSize = 14f
        viewHolder.textViewDescription.setTextColor(Color.WHITE)
        //        Glide.with(viewHolder.itemView)
//                .load(sliderItem.getImageUrl())
//                .placeholder(R.drawable.ic_no_image)
//                .error(R.drawable.ic_error)
//                .fitCenter()
//                .into(viewHolder.imageViewBackground);
        viewHolder.imageViewBackground.loadUrl(viewHolder.itemView, sliderItem.imageUrl)
        viewHolder.itemView.setOnClickListener { v: View ->
            val animation = AlphaAnimation(0.2f, 1.0f)
            animation.duration = 500
            v.alpha = 1f
            v.startAnimation(animation)
            context.showToast("This is item in position $position")
        }
    }

    override fun getCount() = if (mSliderItems == null) 0 else mSliderItems!!.size


    class SliderAdapterVH internal constructor(itemView: View) :
        ViewHolder(itemView) {
        var itemView: View
        var imageViewBackground: ImageView
        var imageGifContainer: ImageView
        var textViewDescription: TextView

        init {
            imageViewBackground =
                itemView.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer =
                itemView.findViewById(R.id.iv_gif_container)
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)
            this.itemView = itemView
        }
    }

}