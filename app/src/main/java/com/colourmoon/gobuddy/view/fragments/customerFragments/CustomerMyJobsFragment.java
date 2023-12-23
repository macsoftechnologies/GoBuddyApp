package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.adapters.CustomerMyJobsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class CustomerMyJobsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout myJobsTabLayout;
    private ViewPager myJobsViewPager;

    private OnFragmentInteractionListener mListener;

    public CustomerMyJobsFragment() {
        // Required empty public constructor
    }

    public static CustomerMyJobsFragment newInstance(String param1, String param2) {
        CustomerMyJobsFragment fragment = new CustomerMyJobsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Orders");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_my_jobs, container, false);

        // this is for casting views from xml to java file
        castingViews(view);

        // this method creates the required tabLayouts
        createTabLayoutItems();

        // this method adds the fragments to the viewpager and adds swiping tab functionality
        setUpViewPager();

        return view;
    }

    private void castingViews(View view) {
        myJobsTabLayout = view.findViewById(R.id.customerMyjobs_tabalyout);
        myJobsViewPager = view.findViewById(R.id.customerJobsViewPager);
    }

    private void setUpViewPager() {
        CustomerMyJobsPagerAdapter customerMyJobsPagerAdapter = new CustomerMyJobsPagerAdapter(getChildFragmentManager());
        myJobsViewPager.setAdapter(customerMyJobsPagerAdapter);
        myJobsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myJobsTabLayout));

        myJobsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myJobsViewPager.setCurrentItem(tab.getPosition());
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
        myJobsTabLayout.addTab(myJobsTabLayout.newTab().setText("ON GOING"));
        myJobsTabLayout.addTab(myJobsTabLayout.newTab().setText("COMPLETED"));
        myJobsTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragmentListener);
    }
}
