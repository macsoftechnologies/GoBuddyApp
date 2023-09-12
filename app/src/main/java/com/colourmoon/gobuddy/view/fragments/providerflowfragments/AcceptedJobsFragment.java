package com.colourmoon.gobuddy.view.fragments.providerflowfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.colourmoon.gobuddy.controllers.providercontrollers.ProviderAcceptedJobFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ProviderAcceptedJobModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.adapters.ProviderAcceptedJobsRecyclerViewAdapter;

import java.util.List;

public class AcceptedJobsFragment extends Fragment implements ProviderAcceptedJobFragmentController.ProviderAcceptedJobFragmentControllerListener, ProviderAcceptedJobsRecyclerViewAdapter.ProviderAcceptedJobItemClickListener {
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
    private RecyclerView acceptedJobsRecyclerView;

    public AcceptedJobsFragment() {
        // Required empty public constructor
    }

    public static AcceptedJobsFragment newInstance(String param1, String param2) {
        AcceptedJobsFragment fragment = new AcceptedJobsFragment();
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
    public void onResume() {
        ProgressBarHelper.show(getActivity(), "Getting Your Jobs");
        ProviderAcceptedJobFragmentController.getInstance().getProviderAcceptedJobsApiCall(
                UserSessionManagement.getInstance(getActivity()).getUserId());
        super.onResume();
    }

    private boolean _hasLoadedOnce = false; // your boolean field

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                ProgressBarHelper.show(getActivity(), "Getting Your Jobs");
                ProviderAcceptedJobFragmentController.getInstance().getProviderAcceptedJobsApiCall(
                        UserSessionManagement.getInstance(getActivity()).getUserId());
                _hasLoadedOnce = true;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accepted_jobs, container, false);

        acceptedJobsRecyclerView = view.findViewById(R.id.providerAcceptedJobsRecyclerView);
        noJobsView = view.findViewById(R.id.accepted_no_jobs_view);
        noJobsImageView = noJobsView.findViewById(R.id.emptyImageView);
        noJobsTextView = noJobsView.findViewById(R.id.empty_textView);

        ProviderAcceptedJobFragmentController.getInstance().setProviderAcceptedJobFragmentControllerListener(this);
        return view;
    }

    @Override
    public void onProviderAcceptedSuccessResponse(List<ProviderAcceptedJobModel> providerAcceptedJobModelList) {
        noJobsView.setVisibility(View.GONE);
        createRecyclerView(providerAcceptedJobModelList);
    }

    private void createRecyclerView(List<ProviderAcceptedJobModel> providerAcceptedJobModelList) {
        acceptedJobsRecyclerView.setHasFixedSize(true);
        acceptedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProviderAcceptedJobsRecyclerViewAdapter providerAcceptedJobsRecyclerViewAdapter = new ProviderAcceptedJobsRecyclerViewAdapter(getActivity(), providerAcceptedJobModelList);
        acceptedJobsRecyclerView.setAdapter(providerAcceptedJobsRecyclerViewAdapter);
        providerAcceptedJobsRecyclerViewAdapter.setProviderAcceptedJobItemClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onProviderAcceptedFailureReason(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
       /* if (failureReason.equalsIgnoreCase("No Jobs Were Accepted")) {
        } else {
            noJobsView.setVisibility(View.GONE);
            Utils.getInstance().showSnackBarOnProviderScreen(failureReason, getActivity());
        }*/
        noJobsView.setVisibility(View.VISIBLE);
        noJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_provider_no_accepted_jobs));
        noJobsTextView.setText(failureReason);
    }

    @Override
    public void onProviderAcceptedDetailsSuccessResponse(ProviderAcceptedJobModel providerAcceptedJobModel) {
        // UnNeccessary overriding
    }

    @Override
    public void onProviderAcceptedItemClick(String orderId, String id) {
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putString("id", id);
        addToFragmentContainer(AcceptedJobDetailsFragment.newInstance(orderId, id), true, "ProviderAcceptedJobDetails");
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
