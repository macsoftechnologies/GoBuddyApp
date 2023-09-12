package com.colourmoon.gobuddy.view.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.colourmoon.gobuddy.view.fragments.CustomerRegistrationFragment;
import com.colourmoon.gobuddy.view.fragments.ProviderRegistrationFragment;

public class RegistrationPagerAdapter extends FragmentStatePagerAdapter {

    public RegistrationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new CustomerRegistrationFragment();
            case 1:
                return new ProviderRegistrationFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
