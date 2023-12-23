package com.colourmoon.gobuddy.view.fragments.customerflowfragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.customercontrollers.AddOrderController;
import com.colourmoon.gobuddy.controllers.customercontrollers.ScheduleTimeFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.colourmoon.gobuddy.utilities.Constants.PLACE_ORDER_FRAGMENT_TAG;

public class ScheduleTimeFragment extends Fragment implements View.OnClickListener, OnRangeSeekbarChangeListener, OnSeekbarChangeListener, ScheduleTimeFragmentController.ScheduleTimeFragmentControllerListener, AddOrderController.AddOrderControllerListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SERVICE_ID_PARAM = "serviceIdParam";
    private static final String SUB_SERVICE_ID_PARAM = "subServiceIdParam";
    private static final String SUB_CATEGORY_PARAM = "subCategoryParam";

    // TODO: Rename and change types of parameters
    private String serviceId;
    private String subServiceId;

    private String[] timeSlotsMainArray;
    private CalendarView scheduleCalendarView;
    private LinearLayout specificLayout, buttonsLayout;
    private RelativeLayout flexibleLayout;
    private boolean isSpecific;
    private String date, timeSlot1, timeslot2;
    private String subcategoryId;

    public ScheduleTimeFragment() {
        // Required empty public constructor
    }

    private TextView specificTimeBtn, flexibleTimeBtn, specificTimeView, maxFlexibleTimeView,
            minFlexibleTimeView, ScheduleTime_nextBtn;
    private CrystalSeekbar specificSeekbar;
    private CrystalRangeSeekbar flexibleSeekbar;

    public static ScheduleTimeFragment newInstance(String serviceId, String subServiceId, String subcategoryId) {
        ScheduleTimeFragment fragment = new ScheduleTimeFragment();
        Bundle args = new Bundle();
        args.putString(SERVICE_ID_PARAM, serviceId);
        args.putString(SUB_SERVICE_ID_PARAM, subServiceId);
        args.putString(SUB_CATEGORY_PARAM, subcategoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceId = getArguments().getString(SERVICE_ID_PARAM);
            subServiceId = getArguments().getString(SUB_SERVICE_ID_PARAM);
            subcategoryId = getArguments().getString(SUB_CATEGORY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_time, container, false);

        castingViews(view);

        flexibleTimeBtn.setOnClickListener(this);
        specificTimeBtn.setOnClickListener(this);
        ScheduleTime_nextBtn.setOnClickListener(this);


        ProgressBarHelper.show(getActivity(), "Loading Slots");
        ScheduleTimeFragmentController.getInstance().getTodayTimeSlots();
        ScheduleTimeFragmentController.getInstance().setScheduleTimeFragmentControllerListener(this);

        scheduleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                ProgressBarHelper.show(getActivity(), "Loading Slots");
                date = dayOfMonth + "/" + month + "/" + year;
                ScheduleTimeFragmentController.getInstance().getTimeSlotsByDate(year + "/" + (month + 1) + "/" + dayOfMonth);
            }
        });
        return view;
    }

    private void castingViews(View view) {
        scheduleCalendarView = view.findViewById(R.id.scheduleCalendarView);
        specificTimeBtn = view.findViewById(R.id.specificTimeBtn);
        flexibleTimeBtn = view.findViewById(R.id.flexibleTimeBtn);
        specificSeekbar = view.findViewById(R.id.specificSeekbar);
        specificTimeView = view.findViewById(R.id.specificTimeView);
        minFlexibleTimeView = view.findViewById(R.id.minFlexibleView);
        maxFlexibleTimeView = view.findViewById(R.id.maxFlexibleView);
        flexibleSeekbar = view.findViewById(R.id.flexibleSeekbar);
        buttonsLayout = view.findViewById(R.id.btnsLayout);
        specificLayout = view.findViewById(R.id.specificLayout);
        flexibleLayout = view.findViewById(R.id.flexibleLayout);
        ScheduleTime_nextBtn = view.findViewById(R.id.scheduleTimeNextBtn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.specificTimeBtn:
                isSpecific = true;
                onSpecificTimeSelected();
                break;
            case R.id.flexibleTimeBtn:
                isSpecific = false;
                onFlexibleTimeSelected();
                break;
            case R.id.scheduleTimeNextBtn:
                if (!UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
                    showSnackBar("Please Login");
                    return;
                }
                AddOrderController.getInstance().addOrderApiCall(createAddOrderMap());
                AddOrderController.getInstance().setAddOrderControllerListener(this);
                break;
            default:
                break;
        }
    }

    private Map<String, String> createAddOrderMap() {
        Map<String, String> addOrderMap = new HashMap<>();
        addOrderMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        addOrderMap.put("service_id", serviceId);
        addOrderMap.put("now", "0");
        addOrderMap.put("sub_service_id", subServiceId.isEmpty() ? "" : subServiceId);
        addOrderMap.put("service_date", date);
        addOrderMap.put("service_time", timeSlot1);
        addOrderMap.put("service_time1", isSpecific ? "" : timeslot2);
        addOrderMap.put("sub_category", subcategoryId);
        addOrderMap.put("schedule", isSpecific ? "specific" : "flexiable");
        addOrderMap.put("recurring_type", "");
        return addOrderMap;
    }

    private void onFlexibleTimeSelected() {
        flexibleTimeBtn.setTextColor(Color.WHITE);
        flexibleTimeBtn.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        specificTimeBtn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        specificTimeBtn.setBackgroundColor(Color.WHITE);
        specificTimeBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.theme_rectangle_box));
        hideSpecificLayout();
    }

    private void onSpecificTimeSelected() {
        specificTimeBtn.setTextColor(Color.WHITE);
        specificTimeBtn.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        flexibleTimeBtn.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        flexibleTimeBtn.setBackgroundColor(Color.WHITE);
        flexibleTimeBtn.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.theme_rectangle_box));
        hideFlexibleLayout();
    }

    private void hideSpecificLayout() {
        specificLayout.setVisibility(View.GONE);
        flexibleLayout.setVisibility(View.VISIBLE);
    }

    private void hideFlexibleLayout() {
        flexibleLayout.setVisibility(View.GONE);
        specificLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void valueChanged(Number minValue, Number maxValue) {
        timeSlot1 = timeSlotsMainArray[Integer.valueOf(String.valueOf(minValue))];
        timeslot2 = timeSlotsMainArray[Integer.valueOf(String.valueOf(maxValue))];
        minFlexibleTimeView.setText(timeSlot1);
        maxFlexibleTimeView.setText(timeslot2);
    }

    @Override
    public void valueChanged(Number value) {
        try {
            timeSlot1 = timeSlotsMainArray[Integer.parseInt(String.valueOf(value))];
            specificTimeView.setText(timeSlot1);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetTimeSlotsSuccessResponse(String[] timeSlotsArray, String date, boolean isDateChanged) {
        this.date = date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date sourceDate = null;
        try {
            sourceDate = simpleDateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(sourceDate.getTime());
            if (!isDateChanged) {
                scheduleCalendarView.setMinDate(calendar.getTimeInMillis());
            } else {
                scheduleCalendarView.setDate(calendar.getTimeInMillis());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        specificSeekbar.setMaxValue(timeSlotsArray.length - 1);
        flexibleSeekbar.setMaxValue(timeSlotsArray.length - 1);
        onSpecificTimeSelected();
        timeSlotsMainArray = new String[timeSlotsArray.length];
        timeSlotsMainArray = timeSlotsArray;
        specificSeekbar.setOnSeekbarChangeListener(this);
        flexibleSeekbar.setOnRangeSeekbarChangeListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onSuccessResponse(String orderId) {
        addToFragmentContainer(PlaceOrderFragment.newInstance(orderId), true, PLACE_ORDER_FRAGMENT_TAG);
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
    public void onFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        showSnackBar(failureReason);
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
        sbView.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorPrimary));
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
    }
}
//Objects.requireNonNull(getActivity()),