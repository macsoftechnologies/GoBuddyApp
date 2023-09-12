package com.colourmoon.gobuddy.view.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.colourmoon.gobuddy.view.fragments.customermyjobsfragments.CustomerCompletedJobsFragment;
import com.colourmoon.gobuddy.view.fragments.customermyjobsfragments.onGoingJobsFragment;

public class CustomerMyJobsPagerAdapter extends FragmentStatePagerAdapter {
    public CustomerMyJobsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new onGoingJobsFragment();
            case 1:
                return new CustomerCompletedJobsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
