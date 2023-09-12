package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.LogoutController;
import com.colourmoon.gobuddy.controllers.commoncontrollers.ViewAsController;
import com.colourmoon.gobuddy.helper.DialogHelper;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.CustomerMainActivity;
import com.colourmoon.gobuddy.view.activities.OnBoardingLoginActivity;
import com.colourmoon.gobuddy.view.activities.ProviderMainActivity;
import com.colourmoon.gobuddy.view.fragments.ProfileFragment;
import com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments.PromoCodeFragment;
import com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments.SaveAddressFragment;
import com.colourmoon.gobuddy.view.fragments.customersettingsflowfragments.SuugestAJobFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerSettingsFragment extends Fragment implements LogoutController.LogoutControllerListener, ViewAsController.ViewAsControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CustomerSettingsFragment() {
        // Required empty public constructor
    }

    private TextView logOutBtn, saveAddressBtn, viewAsProviderBtn, viewProfileBtn, promoCodeBtn;

    public static CustomerSettingsFragment newInstance(String param1, String param2) {
        CustomerSettingsFragment fragment = new CustomerSettingsFragment();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View view = inflater.inflate(R.layout.fragment_customer_settings, container, false);

        castingViews(view);

        if (!UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
            logOutBtn.setText(getResources().getString(R.string.login));
            viewAsProviderBtn.setVisibility(View.GONE);
        }

        view.findViewById(R.id.settings_suggestAjobText).setOnClickListener(view13 -> addToFragmentContainer(new SuugestAJobFragment(), true, "suggestAJobFragment"));

        logOutBtn.setOnClickListener(view12 -> {
            if (logOutBtn.getText().equals(getString(R.string.login))) {
                startActivity(new Intent(getActivity(), OnBoardingLoginActivity.class));
            } else {
                showLogoutAlert();
            }
        });

        saveAddressBtn.setOnClickListener(view1 -> {
            if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
                addToFragmentContainer(new SaveAddressFragment(), true, "saveAddressFragment");
            } else {
                new DialogHelper(getActivity()).showAlert("Please Login To View Addresses", "Alert");
            }
        });

        viewProfileBtn.setOnClickListener(v -> {
            if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
                addToFragmentContainer(ProfileFragment.newInstance("customer"), true, "profileFragment");
            } else {
                new DialogHelper(getActivity()).showAlert("Please Login To View Profile", "Attention");
            }
        });

        viewAsProviderBtn.setOnClickListener(v -> {
            ViewAsController.getInstance().changeViewAsApiCall(createViewAsMap());
        });

        promoCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFragmentContainer(PromoCodeFragment.newInstance("", ""), true, "PromoCode Fragment");
            }
        });


        LogoutController.getInstance().setLogoutControllerListener(this);
        ViewAsController.getInstance().setViewAsControllerListener(this);

        return view;
    }

    private Map<String, String> createViewAsMap() {
        Map<String, String> viewAsMap = new HashMap<>();
        viewAsMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        viewAsMap.put("view_as", "provider");
        return viewAsMap;
    }

    private void showLogoutAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Logout Alert");
        alertDialog.setMessage("Are you sure want to logout now");
        alertDialog.setIcon(R.drawable.ic_logout_icon);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                LogoutController.getInstance().logoutUserApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }

    private void castingViews(View view) {
        logOutBtn = view.findViewById(R.id.settings_logoutText);
        saveAddressBtn = view.findViewById(R.id.settings_savedAddressText);
        viewAsProviderBtn = view.findViewById(R.id.providerSettings_viewAsProviderBtn);
        viewProfileBtn = view.findViewById(R.id.settings_profileText);
        promoCodeBtn = view.findViewById(R.id.settings_promocodeText);
    }

    private void addToFragmentContainer(Fragment fragment, boolean addbackToStack, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addbackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.customer_fragments_container, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
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

    @Override
    public void onLogoutSuccess(String successMessage) {
        if (getActivity() != null) {
            UserSessionManagement.getInstance(getActivity()).logoutUser();
            Intent intent = new Intent(getActivity(), CustomerMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onLogoutFailure(String failureMessage) {
        Utils.getInstance().showSnackBarOnCustomerScreen(failureMessage, getActivity());
    }

    @Override
    public void onViewAsSuccessResponse(String successMessage) {
        UserSessionManagement.getInstance(getActivity()).changeUserType(true);
        Intent intent = new Intent(getActivity(), ProviderMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onViewAsFailureResponse(String failureReason) {
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragmentListener);
    }
}
