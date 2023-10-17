package com.colourmoon.gobuddy.view.fragments.providerflowfragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.colourmoon.gobuddy.controllers.providercontrollers.PayoutsFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.PayoutModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.ProviderMainActivity;
import com.colourmoon.gobuddy.view.adapters.PayoutsRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnSettledPayoutsFragment extends Fragment implements PayoutsFragmentController.PayoutFragmentControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public OnSettledPayoutsFragment() {
        // Required empty public constructor
    }

    public static OnSettledPayoutsFragment newInstance(String param1, String param2) {
        OnSettledPayoutsFragment fragment = new OnSettledPayoutsFragment();
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

    @BindView(R.id.onSettledRecyclerview)
    RecyclerView onSettledRecyclerView;

    private View noJobsView;
    private TextView noJobsTextView;
    private ImageView noJobsImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_settled_payouts, container, false);

        ButterKnife.bind(this, view);
        noJobsView = view.findViewById(R.id.provider_on_settled_noJobs_view);
        noJobsImageView = noJobsView.findViewById(R.id.emptyImageView);
        noJobsTextView = noJobsView.findViewById(R.id.empty_textView);

        PayoutsFragmentController.getInstance().setPayoutFragmentControllerListener(this);

        if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
            ProgressBarHelper.show(getActivity(), "Fetching Payouts");
            PayoutsFragmentController.getInstance().getNotSettledPayoutsApiCall(UserSessionManagement.getInstance(getActivity())
                    .getUserId());
        } else {
            Utils.getInstance().showSnackBarOnProviderScreen("Please Login To View Your Payouts", (ProviderMainActivity) getActivity());
        }

        return view;
    }

    private void createRecyclerView(List<PayoutModel> payoutModelList) {
        onSettledRecyclerView.setHasFixedSize(true);
        onSettledRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PayoutsRecyclerViewAdapter payoutsRecyclerViewAdapter = new PayoutsRecyclerViewAdapter(
                getActivity(), "NotSettled", payoutModelList);
        onSettledRecyclerView.setAdapter(payoutsRecyclerViewAdapter);
        ProgressBarHelper.dismiss(getActivity());
    }


    @Override
    public void onNotSettledPayoutSuccessResponse(List<PayoutModel> payoutModelList) {
        noJobsView.setVisibility(View.GONE);
        createRecyclerView(payoutModelList);
    }

    @Override
    public void onFailureReason(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        noJobsView.setVisibility(View.VISIBLE);
        noJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_provider_payouts_not_settled));
        noJobsTextView.setText(failureReason);
    }

    private boolean _hasLoadedOnce = false; // your boolean field

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                _hasLoadedOnce = true;

            }
        }
    }
}
