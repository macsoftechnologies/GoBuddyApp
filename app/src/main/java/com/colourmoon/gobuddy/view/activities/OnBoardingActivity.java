package com.colourmoon.gobuddy.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.adapters.OnBoardingPagerAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager onBoardingViewPager;
    private LinearLayout dotsLinearLayout;
    private TextView onBoardingNextBtn, onBoardingSkipBtn;
    private TextView[] dotsView;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // this method is responsible for casting all views in xml to java file
        castingViews();

        OnBoardingPagerAdapter onBoardingPagerAdapter = new OnBoardingPagerAdapter(this);
        onBoardingViewPager.setAdapter(onBoardingPagerAdapter);

        onBoardingSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, CustomerMainActivity.class));
                finish();
            }
        });

        // this method add dots to linear layout and changes selection accordingly
        addDotsIndicator(0);

        onBoardingViewPager.addOnPageChangeListener(onPageChangeListener);
        onBoardingViewPager.setPageTransformer(true, new DepthPageTransformer());

        onBoardingNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoardingViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });
    }

    private void castingViews() {
        onBoardingViewPager = findViewById(R.id.onBoardingViewPager);
        dotsLinearLayout = findViewById(R.id.dotsLinearLayout);
        onBoardingNextBtn = findViewById(R.id.onBoardingNextBtn);
        onBoardingSkipBtn = findViewById(R.id.onBoardingSkipBtn);
    }

    private void addDotsIndicator(int position) {
        dotsView = new TextView[5];
        dotsLinearLayout.removeAllViews();
        for (int i = 0; i < dotsView.length; i++) {
            dotsView[i] = new TextView(this);
            dotsView[i].setText(Html.fromHtml("&#9866"));
            dotsView[i].setTextSize(20);
            dotsView[i].setScaleY(2);
            dotsView[i].setTextColor(Color.GRAY);
            dotsLinearLayout.addView(dotsView[i]);
        }
        if (dotsView.length > 0) {
            dotsView[position].setTextColor(getResources().getColor(R.color.colorPrimary));
            dotsView[position].setTextSize(30);
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;
            if (i == dotsView.length - 1) {
                onBoardingNextBtn.setText("");
                onBoardingSkipBtn.setVisibility(View.INVISIBLE);
            } else {
                onBoardingNextBtn.setText("Next");
                onBoardingSkipBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the notification_tone slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the notification_tone slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}
