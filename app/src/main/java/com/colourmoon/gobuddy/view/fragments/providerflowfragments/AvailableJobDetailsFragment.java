package com.colourmoon.gobuddy.view.fragments.providerflowfragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.providercontrollers.AcceptJobController;
import com.colourmoon.gobuddy.controllers.providercontrollers.ProviderAvailableJobFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ProviderAvailableJobModel;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.ProviderMainActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AvailableJobDetailsFragment extends Fragment implements ProviderAvailableJobFragmentController.ProviderAvailableJobFragmentControllerListener, AcceptJobController.AcceptJobControllerListener {

    private static final String ORDER_ID_PARAM = "orderIdParam";
    private static final String ID_PARAM = "idParam";

    // TODO: Rename and change types of parameters
    private String orderId, id;

    public AvailableJobDetailsFragment() {
        // Required empty public constructor
    }

    public static AvailableJobDetailsFragment newInstance(String orderId, String id) {
        AvailableJobDetailsFragment fragment = new AvailableJobDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_ID_PARAM, orderId);
        args.putString(ID_PARAM, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getString(ORDER_ID_PARAM);
            id = getArguments().getString(ID_PARAM);
        }
    }

    //widgets binding using butter knife
    @BindView(R.id.avaiable_details_jobName)
    TextView jobName;

    @BindView(R.id.avaiable_details_jobDate)
    TextView jobDate;

    @BindView(R.id.avaiable_details_ProviderResponsibility)
    TextView providerResponsibility;

    @BindView(R.id.avaiable_details_customerResponsibility)
    TextView customerResponsibility;

    @BindView(R.id.avaialbleJobDetails_cancelBtn)
    TextView cancelBtn;

    @BindView(R.id.avaialbleJobDetails_acceptBtn)
    TextView acceptBtn;

    @BindView(R.id.availableDetails_customerName)
    TextView customerName;

    @BindView(R.id.availableDetails_customerLocation)
    TextView customerLocation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Job ID " + id);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available_job_details, container, false);
        ButterKnife.bind(this, view);

        ProgressBarHelper.show(getActivity(), "Fetching Job Details");
        ProviderAvailableJobFragmentController.getInstance().getAvailableJobDetailsApiCall(orderId);
        ProviderAvailableJobFragmentController.getInstance().setProviderAvailableJobFragmentControllerListener(this);

        AcceptJobController.getInstance().setAcceptJobControllerListener(this);
        cancelBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        acceptBtn.setOnClickListener(v -> {
            ProgressBarHelper.show(getActivity(), "Accepting Job");
            AcceptJobController.getInstance().acceptJobApiCall(createAcceptMap());
        });
        return view;
    }

    private Map<String, String> createAcceptMap() {
        Map<String, String> acceptJobMap = new HashMap<>();
        acceptJobMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
        acceptJobMap.put("order_id", orderId);
        return acceptJobMap;
    }

    @Override
    public void onProviderAvailableSuccessResponse(List<ProviderAvailableJobModel> providerAvailableJobModelList) {
        // neglect this overriding
    }

    @Override
    public void onProviderAvailableDetailsResponse(ProviderAvailableJobModel providerAvailableJobModel) {
        setTextToViews(providerAvailableJobModel);
    }

    @SuppressLint("SetTextI18n")
    private void setTextToViews(ProviderAvailableJobModel providerAvailableJobModel) {
        if (!providerAvailableJobModel.getSubServiceTitle().equalsIgnoreCase("null")) {
            jobName.setText(providerAvailableJobModel.getServiceTitle() + " -> " + providerAvailableJobModel.getSubServiceTitle());
        } else {
            jobName.setText(providerAvailableJobModel.getServiceTitle());
        }
        jobDate.setText(providerAvailableJobModel.getDateAndTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            providerResponsibility.setText(Html.fromHtml(providerAvailableJobModel.getProviderResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            providerResponsibility.setText(Html.fromHtml(providerAvailableJobModel.getProviderResponsibility()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            customerResponsibility.setText(Html.fromHtml(providerAvailableJobModel.getCustomerResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            customerResponsibility.setText(Html.fromHtml(providerAvailableJobModel.getCustomerResponsibility()));
        }
        customerName.setText(providerAvailableJobModel.getGender() + " " + providerAvailableJobModel.getCustomerName());
        customerLocation.setText(providerAvailableJobModel.getLocality());
        ProgressBarHelper.dismiss(getActivity());

    }

    @Override
    public void onProviderAvailableFailureReason(String failureReason) {
        Utils.getInstance().showSnackBarOnProviderScreen(failureReason, getActivity());
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onAcceptJobSuccessResponse(String successResponse) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnProviderScreen(successResponse, getActivity());
        if (getActivity().getIntent().hasExtra("screen_type")) {
            ((ProviderMainActivity) getActivity()).changeProviderHomeSelection(R.id.provider_pending_jobs);
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        AcceptJobController.getInstance().acceptJobNotificationApiCall(createAcceptMap());
    }

    @Override
    public void onAcceptJobFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnProviderScreen(failureReason, getActivity());
    }
}
