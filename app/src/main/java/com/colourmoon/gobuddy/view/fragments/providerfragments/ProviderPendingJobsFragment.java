package com.colourmoon.gobuddy.view.fragments.providerfragments;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.adapters.ProviderPendingJobsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ProviderPendingJobsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout providerPendingJobsTabLayout;
    private ViewPager providerPendingJobsViewPager;

    public ProviderPendingJobsFragment() {
        // Required empty public constructor
    }

    public static ProviderPendingJobsFragment newInstance(String param1, String param2) {
        ProviderPendingJobsFragment fragment = new ProviderPendingJobsFragment();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pending Jobs");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_pending_jobs, container, false);

        // this is for casting views from xml to java file
        castingViews(view);

        // this method creates the required tabLayouts
        createTabLayoutItems();

        // this method adds the fragments to the viewpager and adds swiping tab functionality
        setUpViewPager();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void castingViews(View view) {
        providerPendingJobsTabLayout = view.findViewById(R.id.providerPendingJobsTabLayout);
        providerPendingJobsViewPager = view.findViewById(R.id.providerPendingJobsViewPager);
    }

    private void setUpViewPager() {
        ProviderPendingJobsPagerAdapter providerPendingJobsPagerAdapter = new ProviderPendingJobsPagerAdapter(getChildFragmentManager());
        providerPendingJobsViewPager.setAdapter(providerPendingJobsPagerAdapter);
        providerPendingJobsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(providerPendingJobsTabLayout));

        providerPendingJobsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                providerPendingJobsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void changeTab() {
        providerPendingJobsViewPager.setCurrentItem(2);
    }

    private void createTabLayoutItems() {
        providerPendingJobsTabLayout.addTab(providerPendingJobsTabLayout.newTab().setText("Available Jobs"));
        providerPendingJobsTabLayout.addTab(providerPendingJobsTabLayout.newTab().setText("Accepted Jobs"));
        providerPendingJobsTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }
}
