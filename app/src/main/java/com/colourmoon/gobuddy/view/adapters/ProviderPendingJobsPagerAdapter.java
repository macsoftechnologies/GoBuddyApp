package com.colourmoon.gobuddy.view.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.colourmoon.gobuddy.view.fragments.providerflowfragments.AcceptedJobsFragment;
import com.colourmoon.gobuddy.view.fragments.providerflowfragments.AvailableJobsFragment;

public class ProviderPendingJobsPagerAdapter extends FragmentStatePagerAdapter {

    public ProviderPendingJobsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AvailableJobsFragment();
            case 1:
                return new AcceptedJobsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
