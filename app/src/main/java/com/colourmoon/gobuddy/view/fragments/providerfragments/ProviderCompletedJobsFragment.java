package com.colourmoon.gobuddy.view.fragments.providerfragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.providercontrollers.ProviderCompletedJobsFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ProviderCompletedJobModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.view.adapters.ProviderCompletedJobsRecyclerViewAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProviderCompletedJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderCompletedJobsFragment extends Fragment implements ProviderCompletedJobsFragmentController.ProviderCompletedJobsFragmentControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // widgets
    private RecyclerView providerCompletedJobsRecyclerView;
    private View noJobsView;
    private TextView noJobsTextView;
    private ImageView noJobsImageView;

    public ProviderCompletedJobsFragment() {
        // Required empty public constructor
    }

    public static ProviderCompletedJobsFragment newInstance(String param1, String param2) {
        ProviderCompletedJobsFragment fragment = new ProviderCompletedJobsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Completed Jobs");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_provider_completed_jobs, container, false);

        providerCompletedJobsRecyclerView = view.findViewById(R.id.providerCompletedJobsRecyclerView);
        noJobsView = view.findViewById(R.id.provider_completed_noJobs_view);
        noJobsImageView = noJobsView.findViewById(R.id.emptyImageView);
        noJobsTextView = noJobsView.findViewById(R.id.empty_textView);

        ProgressBarHelper.show(getActivity(), "Fetching Your Completed Jobs");
        ProviderCompletedJobsFragmentController.getInstance().getProviderCompletedJobsApiCall(
                UserSessionManagement.getInstance(getActivity()).getUserId());
        ProviderCompletedJobsFragmentController.getInstance().setProviderCompletedJobsFragmentControllerListener(this);
        return view;
    }

    @Override
    public void onProviderCompletedJobsSuccessResponse(List<ProviderCompletedJobModel> providerCompletedJobModelList) {
        noJobsView.setVisibility(View.GONE);
        createRecyclerView(providerCompletedJobModelList);
    }

    private void createRecyclerView(List<ProviderCompletedJobModel> providerCompletedJobModelList) {
        providerCompletedJobsRecyclerView.setHasFixedSize(true);
        providerCompletedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProviderCompletedJobsRecyclerViewAdapter providerCompletedJobsRecyclerViewAdapter = new ProviderCompletedJobsRecyclerViewAdapter(
                getActivity(), providerCompletedJobModelList);
        providerCompletedJobsRecyclerView.setAdapter(providerCompletedJobsRecyclerViewAdapter);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onFailureReason(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        noJobsView.setVisibility(View.VISIBLE);
        noJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_provider_no_completed_jobs));
        noJobsTextView.setText(failureReason);
    }
}
