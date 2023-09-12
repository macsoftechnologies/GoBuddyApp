package com.colourmoon.gobuddy.view.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.colourmoon.gobuddy.view.fragments.providerflowfragments.OnSettledPayoutsFragment;
import com.colourmoon.gobuddy.view.fragments.providerflowfragments.SetteldPayoutsFragment;

public class PayoutsViewPagerAdapter extends FragmentStatePagerAdapter {

    public PayoutsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OnSettledPayoutsFragment();
            case 1:
                return new SetteldPayoutsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
