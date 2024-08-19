package com.colourmoon.gobuddy.view.fragments.customerFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.OnGoingFragmentController;
import com.colourmoon.gobuddy.model.CustomerJobModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.CustomerMyJobsPagerAdapter;
import com.colourmoon.gobuddy.view.fragments.customermyjobsfragments.CustomerJobDetailsFragment;
import com.google.android.material.tabs.TabLayout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerMyJobsFragment extends Fragment implements PaymentResultListener,OnGoingFragmentController.OnGoingFragmentControllerListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TabLayout myJobsTabLayout;
    private ViewPager myJobsViewPager;
    private String id;
    private OnFragmentInteractionListener mListener;

    public CustomerMyJobsFragment() {
        // Required empty public constructor
    }

    public static CustomerMyJobsFragment newInstance(String param1, String param2) {
        CustomerMyJobsFragment fragment = new CustomerMyJobsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Orders");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_my_jobs, container, false);

        // Casting views from XML to Java
        castingViews(view);

        // Making the API call to get ongoing jobs
        OnGoingFragmentController.getInstance().getOnGoingJobsApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
        OnGoingFragmentController.getInstance().setOnGoingFragmentControllerListener(new OnGoingFragmentController.OnGoingFragmentControllerListener() {
            @Override
            public void onGoingJobsSuccessResponse(List<CustomerJobModel> customerJobModelList) {
                if (isAdded()) {
                    for (CustomerJobModel jobModel : customerJobModelList) {
                        String ids = jobModel.getJobId();
                       // Toast.makeText(getContext(), "id " + ids, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onGoingJobsFailureResponse(String failureReason) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Failed: " + failureReason, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Creating TabLayout items
        createTabLayoutItems();

        // Setting up the ViewPager with the adapter
        setUpViewPager();

        return view;
    }

    private void castingViews(View view) {
        myJobsTabLayout = view.findViewById(R.id.customerMyjobs_tabalyout);
        myJobsViewPager = view.findViewById(R.id.customerJobsViewPager);
    }

    private void setUpViewPager() {
        CustomerMyJobsPagerAdapter customerMyJobsPagerAdapter = new CustomerMyJobsPagerAdapter(getChildFragmentManager());
        myJobsViewPager.setAdapter(customerMyJobsPagerAdapter);
        myJobsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(myJobsTabLayout));

        myJobsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                myJobsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void createTabLayoutItems() {
        myJobsTabLayout.addTab(myJobsTabLayout.newTab().setText("ON GOING"));
        myJobsTabLayout.addTab(myJobsTabLayout.newTab().setText("COMPLETED"));
        myJobsTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }
    @Override
    public void onGoingJobsSuccessResponse(List<CustomerJobModel> customerJobModelList) {
         id = customerJobModelList.get(0).getJobId();

    }

    @Override
    public void onGoingJobsFailureResponse(String failureReason) {

    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        id = UserSessionManagement.getInstance(getActivity()).getJobid();
        Toast.makeText(getContext(), "Payment Successful: " , Toast.LENGTH_LONG).show();

        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> jobDoneCall = goBuddyApiInterface.jobDoneByCustomer(id);
        jobDoneCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            if (isAdded()) {
                                addToFragmentContainer(CustomerJobDetailsFragment.newInstance(id), true, "CompletedJobsFragment");
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (isAdded()) {
                    Utils.getInstance().showSnackBarOnCustomerScreen(t.getLocalizedMessage(), getActivity());
                }
            }
        });
    }

    @Override
    public void onPaymentError(int code, String response) {
        int currentItem = myJobsViewPager.getCurrentItem();
        Fragment currentFragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.customerJobsViewPager + ":" + currentItem);
        if (currentFragment instanceof PaymentResultListener) {
            ((PaymentResultListener) currentFragment).onPaymentError(code, response);
        } else {
            Toast.makeText(getActivity(), "Payment failed: " + response, Toast.LENGTH_LONG).show();
        }
    }

    private void addToFragmentContainer(Fragment fragment, boolean addBackToStack, String tag) {
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (addBackToStack) {
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.customer_fragments_container, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            Log.e("CustomerMyJobsFragment", "Activity is null or fragment is not attached");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String fragmentListener);
    }
}
