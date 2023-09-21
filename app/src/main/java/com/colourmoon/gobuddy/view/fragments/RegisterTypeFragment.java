package com.colourmoon.gobuddy.view.fragments;

import static com.colourmoon.gobuddy.utilities.Constants.CUSTOMER_REGISTRATION_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.EKYC_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.PROVIDER_REGISTRATION_TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.view.fragments.ProvPostRegFragments.UpdateEKycFragment;


public class RegisterTypeFragment extends Fragment {

    TextView txtCustomer,txtVendor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        castingViews(view);

        txtCustomer.setOnClickListener(view1 -> {
            addToFragmentContainer(new CustomerRegistrationFragment(), true, CUSTOMER_REGISTRATION_TAG);
        });

        txtVendor.setOnClickListener(view1 -> {
            addToFragmentContainer(new CustomerRegistrationFragment(), true, PROVIDER_REGISTRATION_TAG);
        });

    }

    private void castingViews(View view) {
        txtCustomer = view.findViewById(R.id.txtCustomer);
        txtVendor = view.findViewById(R.id.txtVendor);
    }

    private void addToFragmentContainer(Fragment fragment, boolean addbackToStack, String tag) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addbackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.replace(R.id.postRegistrationFragmentContainer, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }
}