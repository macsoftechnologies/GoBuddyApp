package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.AddOrderController;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;

import java.util.HashMap;
import java.util.Map;

import static com.colourmoon.gobuddy.utilities.Constants.PLACE_ORDER_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.SCHEDULE_RECURRING_FRAGMENT_TAG;
import static com.colourmoon.gobuddy.utilities.Constants.SCHEDULE_TIME_FRAGMENT_TAG;


public class ScheduleServiceFragment extends Fragment implements View.OnClickListener, AddOrderController.AddOrderControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String SERVICE_PARAM = "serviceParam";
    private static final String SUB_SERVICE_PARAM = "subServiceParam";
    private static final String SUB_CATEGORY_PARAM = "subCategoryParam";

    // TODO: Rename and change types of parameters
    private String serviceId;
    private String subServiceId;
    private String subcategoryId;

    private TextView scheduleNowBtn, scheduleTimeBtn, scheduleRecurringBtn;

    public ScheduleServiceFragment() {
        // Required empty public constructor
    }

    public static ScheduleServiceFragment newInstance(String serviceId, String subServiceId, String subCategoryId) {
        ScheduleServiceFragment fragment = new ScheduleServiceFragment();
        Bundle args = new Bundle();
        args.putString(SERVICE_PARAM, serviceId);
        args.putString(SUB_SERVICE_PARAM, subServiceId);
        args.putString(SUB_CATEGORY_PARAM, subCategoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceId = getArguments().getString(SERVICE_PARAM);
            subServiceId = getArguments().getString(SUB_SERVICE_PARAM);
            subcategoryId = getArguments().getString(SUB_CATEGORY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Schedule");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        castingViews(view);

        scheduleNowBtn.setOnClickListener(this);
        scheduleTimeBtn.setOnClickListener(this);
        scheduleRecurringBtn.setOnClickListener(this);
        return view;
    }

    private void castingViews(View view) {
        scheduleNowBtn = view.findViewById(R.id.schedule_nowBtn);
        scheduleTimeBtn = view.findViewById(R.id.schedule_timeBtn);
        scheduleRecurringBtn = view.findViewById(R.id.schedule_recurringBtn);
        //  scheduleNextBtn = view.findViewById(R.id.scheduleNextBtn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schedule_nowBtn:
                if (!UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
                    showSnackBar("Please Login");
                } else {
                    AddOrderController.getInstance().addOrderApiCall(createAddOrderMap());
                    AddOrderController.getInstance().setAddOrderControllerListener(this);
                }
                break;
            case R.id.schedule_timeBtn:
                addToFragmentContainer(ScheduleTimeFragment.newInstance(serviceId, subServiceId, subcategoryId), true, SCHEDULE_TIME_FRAGMENT_TAG);
                break;
            case R.id.schedule_recurringBtn:
                addToFragmentContainer(ScheduleRecurringFragment.newInstance(serviceId, subServiceId, subcategoryId), true, SCHEDULE_RECURRING_FRAGMENT_TAG);
                break;
            default:
                break;
        }
    }

    private Map<String, String> createAddOrderMap() {
        Map<String, String> addOrderMap = new HashMap<>();
        addOrderMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        addOrderMap.put("service_id", serviceId);
        addOrderMap.put("now", "1");
        addOrderMap.put("sub_service_id", subServiceId.isEmpty() ? "" : subServiceId);
        addOrderMap.put("service_date", "");
        addOrderMap.put("service_time", "");
        addOrderMap.put("service_time1", "");
        addOrderMap.put("schedule", "specific");
        addOrderMap.put("sub_category", subcategoryId);
        addOrderMap.put("recurring_type", "");
        return addOrderMap;
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

    public void showSnackBar(String message) {
        CoordinatorLayout coordinatorLayout = getActivity().findViewById(R.id.customerFragments_coordinator_layout);
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }

    @Override
    public void onSuccessResponse(String orderId) {
        addToFragmentContainer(PlaceOrderFragment.newInstance(orderId), true, PLACE_ORDER_FRAGMENT_TAG);
    }

    @Override
    public void onFailureResponse(String failureReason) {
        showSnackBar(failureReason);
    }
}
