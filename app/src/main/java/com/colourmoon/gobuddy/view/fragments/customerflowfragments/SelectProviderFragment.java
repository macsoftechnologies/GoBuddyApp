package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.SelectProviderFragmentController;
import com.colourmoon.gobuddy.model.ProviderModel;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.ProviderListRecyclerAdapter;
import com.colourmoon.gobuddy.view.fragments.customerFragments.CustomerHomeFragment;

import java.util.List;

public class SelectProviderFragment extends Fragment implements SelectProviderFragmentController.SelectFragmentControllerListener, ProviderListRecyclerAdapter.ProviderListItemClickListener {

    private static final String ORDER_ID_PARAM = "orderIdParam";

    private String orderId;
    private RecyclerView providersRecyclerview;

    public SelectProviderFragment() {
        // Required empty public constructor
    }

    private TextView orderIdTextView, displayTextView;

    public static SelectProviderFragment newInstance(String orderId) {
        SelectProviderFragment fragment = new SelectProviderFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_ID_PARAM, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getString(ORDER_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Provider");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        redirectToHomeAfterDelay();

        castingViews(view);

        SelectProviderFragmentController.getInstance().orderConfirmedApiCall(orderId);
        SelectProviderFragmentController.getInstance().setSelectFragmentControllerListener(this);

        return view;
    }

    private void redirectToHomeAfterDelay() {
        final int REDIRECT_DELAY_MILLIS = 4000; // 2 seconds delay

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectToHomeFragment();
            }
        }, REDIRECT_DELAY_MILLIS);
    }

    private void castingViews(View view) {
        orderIdTextView = view.findViewById(R.id.orderIdView);
        displayTextView = view.findViewById(R.id.displayTextView);
        providersRecyclerview = view.findViewById(R.id.providersRecyclerView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccessResponse(List<ProviderModel> providerModelList, String orderId, String displayText) {
        orderIdTextView.setText("Order ID #" + orderId);
        displayTextView.setText(displayText);
        createRecyclerView(providerModelList);
    }

    private void createRecyclerView(List<ProviderModel> providerModelList) {
        providersRecyclerview.setHasFixedSize(true);
        providersRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProviderListRecyclerAdapter providerListRecyclerAdapter = new ProviderListRecyclerAdapter(getActivity(), providerModelList);
        providersRecyclerview.setAdapter(providerListRecyclerAdapter);
        providerListRecyclerAdapter.setProviderListItemClickListener(this);
    }

    @Override
    public void onFailureResponse(String failureReason) {
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
    }

    @Override
    public void onProviderItemClick(ProviderModel providerModel) {
        addToFragmentContainer(ViewProviderFragment.newInstance(providerModel.getProviderId()), true, "ViewProviderFragmentTag");
    }
    private void redirectToHomeFragment() {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the current fragment with your HomeFragment
            CustomerHomeFragment homeFragment = new CustomerHomeFragment();
            fragmentTransaction.replace(R.id.customer_fragments_container, homeFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
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
}
