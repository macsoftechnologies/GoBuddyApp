package com.colourmoon.gobuddy.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.activities.CustomerMainActivity;

public class OnBoardingPagerAdapter extends PagerAdapter {

    private Context context;

    private int[] onBoardingImages = {
            R.drawable.first_boarding, R.drawable.second_boarding, R.drawable.third_boarding, R.drawable.fourth_boarding, R.drawable.fifth_boarding
    };

    private String[] onBoardingHeadings = {
            "GOBUDDY", "Make money on your own schedule as a Provider", "GOBUDDY", "GOBUDDY", "GOBUDDY"
    };

    /*   private String[] onBoardingDescriptions = {
               "Simplified and Convenient Platform to get help fast on your schedule Just order what help you need, and we will get a trusted local provider on your way to get your job done. all providers are background checked",
               "Whether you want to become an entrepreneur and work for yourself or you already have a full-time job and just hope to make some extra cash in your free time, you can use GOBUDDY platform to register as a provider to put your skills to work hassle free.",
               "Say goodbye to estimates. All Jobs are pre-priced and pre- defined to avoid surprises and you know what to expect. You can choose to Pay through the App Or Pay to the Provider directly after the job is completed",
               "Providing you the best experience is our highest priority. if you aren't satisfied, we will work with you to make it right. Please provide us the feedback, we are listening and improving",
               "Get the help you need-fast, easy and click(s)-away"
       };*/
    private String[] onBoardingDescriptions = {
            "Order and Schedule the Help you need in few Simple Clicks, Pre-Listed Services and Upfront Pricing",
            "Do not see the Service you need, no problem let us know by submitting “Suggest a Job” from your Settings screen",
            "Local and Trusted Providers, all background checked\nGOBUDDY guarantees yours satisfaction and goes beyond to put a Happy Smile on you always",
            "Register as a Provider for one or more of our Services and start earning money, be your own Boss!",
            "Thank you for Downloading. You are about to Improve Your Living!\n" +
                    "Congratulations!"
    };

    public OnBoardingPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return onBoardingImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.custom_onboarding_page, container, false);
        ImageView customOnBoardingImage = view.findViewById(R.id.onBoardingSliderImage);
        TextView customOnBoardingHeading = view.findViewById(R.id.onBoardingHead);
        TextView customOnBoardingDesc = view.findViewById(R.id.onBoardingDesc);
        TextView onBoardingGetStartedBtn = view.findViewById(R.id.onBoardingGetStartedBtn);

        customOnBoardingImage.setImageResource(onBoardingImages[position]);
        customOnBoardingHeading.setText(onBoardingHeadings[position]);
        customOnBoardingDesc.setText(onBoardingDescriptions[position]);

        if (position == (onBoardingImages.length - 1)) {
            onBoardingGetStartedBtn.setVisibility(View.VISIBLE);
            onBoardingGetStartedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, CustomerMainActivity.class));
                    ((Activity) context).finish();
                }
            });
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
