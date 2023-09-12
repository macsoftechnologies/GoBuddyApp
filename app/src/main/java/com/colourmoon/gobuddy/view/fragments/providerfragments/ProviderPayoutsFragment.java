package com.colourmoon.gobuddy.view.fragments.providerfragments;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.adapters.PayoutsViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProviderPayoutsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProviderPayoutsFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.providerPayoutsViewPager)
    ViewPager payoutsViewPager;

    @BindView(R.id.payoutsTabsLayout)
    TabLayout payoutsTabsLayout;

    public static ProviderPayoutsFragment newInstance(String param1, String param2) {
        ProviderPayoutsFragment fragment = new ProviderPayoutsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Payouts");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_payouts, container, false);

        ButterKnife.bind(this, view);
        // for creating tab items
        createTabLayoutItems();

        // for setting up view pager
        setUpViewPager();

        return view;
    }

    private void setUpViewPager() {

        PayoutsViewPagerAdapter registrationPagerAdapter = new PayoutsViewPagerAdapter(getChildFragmentManager());
        payoutsViewPager.setAdapter(registrationPagerAdapter);
        payoutsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(payoutsTabsLayout));

        payoutsTabsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                payoutsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void createTabLayoutItems() {
       /* Spannable onSetteldString = new SpannableString("NOT SETTLED\n" + "Rs.3000");
        onSetteldString.setSpan(new ForegroundColorSpan(Color.RED), 11, onSetteldString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        payoutsTabsLayout.addTab(payoutsTabsLayout.newTab().setText("NOT SETTLED"));
       /* Spannable settledString = new SpannableString("SETTLED\n" + "Rs.3000");
        onSetteldString.setSpan(new ForegroundColorSpan(Color.RED), 7, settledString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        payoutsTabsLayout.addTab(payoutsTabsLayout.newTab().setText("SETTLED"));
        payoutsTabsLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

}
