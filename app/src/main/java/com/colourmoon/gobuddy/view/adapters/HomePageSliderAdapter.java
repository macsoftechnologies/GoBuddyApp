package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.model.ImageSliderModel;

import java.util.ArrayList;

public class HomePageSliderAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<ImageSliderModel> imageSliderModelArrayList;

    public HomePageSliderAdapter(Context mContext, ArrayList<ImageSliderModel> mImageSliderModelArrayList) {
        this.context = mContext;
        this.imageSliderModelArrayList = mImageSliderModelArrayList;
    }

    @Override
    public int getCount() {
        return imageSliderModelArrayList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        Glide.with(context)
                .load(imageSliderModelArrayList.get(position).getImage_url())
                .fitCenter()
                .centerCrop()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
