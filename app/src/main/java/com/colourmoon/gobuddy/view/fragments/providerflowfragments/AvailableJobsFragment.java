package com.colourmoon.gobuddy.view.fragments.providerflowfragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.providercontrollers.ProviderAvailableJobFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ProviderAvailableJobModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.adapters.ProviderAvailableJobsRecyclerViewAdapter;

import java.util.List;
import java.util.Objects;

public class AvailableJobsFragment extends Fragment implements ProviderAvailableJobFragmentController.ProviderAvailableJobFragmentControllerListener,
        ProviderAvailableJobsRecyclerViewAdapter.ProviderAvailableItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View noJobsView;
    private TextView noJobsTextView;
    private ImageView noJobsImageView;

    // widgets
    private RecyclerView availableJobsRecyclerView;

    public AvailableJobsFragment() {
        // Required empty public constructor
    }

    public static AvailableJobsFragment newInstance(String param1, String param2) {
        AvailableJobsFragment fragment = new AvailableJobsFragment();
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available_jobs, container, false);

        noJobsView = view.findViewById(R.id.available_no_jobs_view);
        noJobsImageView = noJobsView.findViewById(R.id.emptyImageView);
        noJobsTextView = noJobsView.findViewById(R.id.empty_textView);
        availableJobsRecyclerView = view.findViewById(R.id.providerAvailableJobsRecyclerView);

        ProviderAvailableJobFragmentController.getInstance().setProviderAvailableJobFragmentControllerListener(this);

        return view;
    }

    @Override
    public void onResume() {
        ProgressBarHelper.show(getActivity(), "Getting Your Jobs");
        ProviderAvailableJobFragmentController.getInstance().getProviderAvailableJobsApiCall(
                UserSessionManagement.getInstance(getActivity()).getUserId());
        super.onResume();
    }

    @Override
    public void onProviderAvailableSuccessResponse(List<ProviderAvailableJobModel> providerAvailableJobModelList) {
        noJobsView.setVisibility(View.GONE);
        createRecyclerView(providerAvailableJobModelList);
    }

    @Override
    public void onProviderAvailableDetailsResponse(ProviderAvailableJobModel providerAvailableJobModel) {

    }

    private void createRecyclerView(List<ProviderAvailableJobModel> providerAvailableJobModelList) {
        availableJobsRecyclerView.setHasFixedSize(true);
        availableJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProviderAvailableJobsRecyclerViewAdapter providerAvailableJobsRecyclerViewAdapter = new ProviderAvailableJobsRecyclerViewAdapter(
                getActivity(), providerAvailableJobModelList);
        availableJobsRecyclerView.setAdapter(providerAvailableJobsRecyclerViewAdapter);
        providerAvailableJobsRecyclerViewAdapter.setProviderAvailableItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProviderAvailableFailureReason(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        noJobsView.setVisibility(View.VISIBLE);
        noJobsImageView.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_provider_no_available_jobs));
        noJobsTextView.setText(failureReason);
    }

    @Override
    public void onProviderItemClick(String orderId, String id) {
        addToFragmentContainer(AvailableJobDetailsFragment.newInstance(orderId, id), true,
                "AvaialbleJobDetails");
    }

    private void addToFragmentContainer(Fragment fragment, boolean addBackToStack, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addBackToStack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.providerFragmentsContainer, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
