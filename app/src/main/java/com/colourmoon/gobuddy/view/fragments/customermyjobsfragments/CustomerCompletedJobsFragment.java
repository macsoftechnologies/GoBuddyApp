package com.colourmoon.gobuddy.view.fragments.customermyjobsfragments;


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
import com.colourmoon.gobuddy.controllers.customercontrollers.CustomerCompletedFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.CustomerJobModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.CustomerOnGoingJobsAdapter;

import java.util.List;

public class CustomerCompletedJobsFragment extends Fragment implements CustomerCompletedFragmentController.CustomerCompletedFragmentControllerListener, CustomerOnGoingJobsAdapter.CustomerOnGoingAdapterOnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView makeFavoritesBtn;
    private List<CustomerJobModel> customerJobModelList;


    public CustomerCompletedJobsFragment() {
        // Required empty public constructor
    }

    public static CustomerCompletedJobsFragment newInstance(String param1, String param2) {
        CustomerCompletedJobsFragment fragment = new CustomerCompletedJobsFragment();
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

    private RecyclerView completedJobsRecyclerView;
    private View noJobsView;
    private TextView noJobsTextView;
    private ImageView noJobsImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_completed_jobs, container, false);

        completedJobsRecyclerView = view.findViewById(R.id.customerCompletedJobsRecyclerView);
        noJobsView = view.findViewById(R.id.customer_completed_noJobs_view);
        noJobsImageView = noJobsView.findViewById(R.id.emptyImageView);
        noJobsTextView = noJobsView.findViewById(R.id.empty_textView);

        return view;
    }


    private void createRecyclerView(List<CustomerJobModel> customerJobModelList) {
        completedJobsRecyclerView.setHasFixedSize(true);
        completedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CustomerOnGoingJobsAdapter customerOnGoingJobsAdapter = new CustomerOnGoingJobsAdapter(getActivity(), customerJobModelList, "completed");
        completedJobsRecyclerView.setAdapter(customerOnGoingJobsAdapter);
        customerOnGoingJobsAdapter.setCustomerOnGoingAdapterOnClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onCustomerCompletedJobsResponse(List<CustomerJobModel> customerJobModelList) {
        noJobsView.setVisibility(View.GONE);
        createRecyclerView(customerJobModelList);
        this.customerJobModelList = customerJobModelList;
    }

    @Override
    public void onMakeFavoriteSuccessResponse(String successResponse, int position) {
        Utils.getInstance().showSnackBarOnCustomerScreen(successResponse, getActivity());
    }

    @Override
    public void onMakeFavoriteFailureResponse(String failureResponse, int position) {
        CustomerJobModel customerJobModel = customerJobModelList.get(position);
        if (getActivity() != null) {
            if (customerJobModel.getIsFavourite().equalsIgnoreCase("1")) {
                makeFavoritesBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorites_un_selected));
            } else {
                makeFavoritesBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorites_selected));
            }
        }
    }

    @Override
    public void onCustomerCompletedJobsFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        noJobsView.setVisibility(View.VISIBLE);
        noJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_provider_payouts_not_settled));
        noJobsTextView.setText(failureReason);
    }

    @Override
    public void onOnGoingJobItemClick(CustomerJobModel customerJobModel) {
        addToFragmentContainer(CustomerJobDetailsFragment.newInstance(customerJobModel.getJobId()), true,
                "CompletedJobDetailsFragment");
    }

    @Override
    public void onPayNowItemClick(CustomerJobModel customerJobModel) {

    }

    @Override
    public void onMakeFavouriteBtnClick(ImageView favouritesBtn, int position) {
        CustomerJobModel customerJobModel = customerJobModelList.get(position);
        makeFavoritesBtn = favouritesBtn;
        CustomerCompletedFragmentController.getInstance().postFavouritesApiCall(favouritesBtn,
                customerJobModel.getJobId(), position);
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
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_) {
                if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
                    ProgressBarHelper.show(getActivity(), "Fetching Your Completed Jobs");
                    CustomerCompletedFragmentController.getInstance().getCompletedJobsApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
                    CustomerCompletedFragmentController.getInstance().setCustomerCompletedFragmentControllerListener(this);
                } else {
                    Utils.getInstance().showSnackBarOnCustomerScreen("Please Login to View Completed Jobs", getActivity());
                }
                //  _hasLoadedOnce = true;
            }
        }
    }
}
