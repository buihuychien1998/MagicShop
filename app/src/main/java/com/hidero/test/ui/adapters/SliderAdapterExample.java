package com.hidero.test.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hidero.test.R;
import com.hidero.test.data.valueobject.SliderItem;
import com.hidero.test.util.UtilsKt;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapterExample(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider_layout, parent, false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        SliderItem sliderItem = mSliderItems.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getDescription());
        viewHolder.textViewDescription.setTextSize(14);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);
//        Glide.with(viewHolder.itemView)
//                .load(sliderItem.getImageUrl())
//                .placeholder(R.drawable.ic_no_image)
//                .error(R.drawable.ic_error)
//                .fitCenter()
//                .into(viewHolder.imageViewBackground);
        UtilsKt.loadUrl(viewHolder.imageViewBackground, viewHolder.itemView, sliderItem.getImageUrl());
        viewHolder.itemView.setOnClickListener(v -> {
            AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
            animation.setDuration(500);
            v.setAlpha(1f);
            v.startAnimation(animation);
            UtilsKt.showToast(context, "This is item in position " + position);
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems == null ? 0: mSliderItems.size();
    }

    public static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}