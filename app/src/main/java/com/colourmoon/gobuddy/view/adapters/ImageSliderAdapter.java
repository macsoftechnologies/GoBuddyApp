/*package com.colourmoon.gobuddy.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import com.colourmoon.gobuddy.R;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private int[] imageIds = {R.drawable.amazon_banner, R.drawable.flipkart_sale_banner_0, R.drawable. adds}; // Replace with your image resource IDs

    public ImageSliderAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return imageIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageIds[position]);

        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}*/