package com.colourmoon.gobuddy.view.fragments.providerflowfragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.providercontrollers.ProviderAcceptedJobFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ProviderAcceptedJobModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.ChatActivity;
import com.colourmoon.gobuddy.view.activities.ProviderMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedJobDetailsFragment extends Fragment implements ProviderAcceptedJobFragmentController.ProviderAcceptedJobFragmentControllerListener, View.OnClickListener {

    private static final String ORDERID_PARAM = "orderIdParam";
    private static final String ID_PARAM = "idParam";

    // TODO: Rename and change types of parameters
    private String orderId, id, extraAmount;

    private ProviderAcceptedJobModel acceptedJobModel;
    //widgets
    private TextView jobNameView, jobDateView, providerResponsibilityView, customerResponsiblityView,
            customerNameView, customerLocationView, providerJobDoneBtn;
    private ImageView viewCustomerInMapBtn, chatWithCustomerBtn, callCustomerBtn;

    public AcceptedJobDetailsFragment() {
        // Required empty public constructor
    }

    public static AcceptedJobDetailsFragment newInstance(String orderId, String id) {
        AcceptedJobDetailsFragment fragment = new AcceptedJobDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ORDERID_PARAM, orderId);
        args.putString(ID_PARAM, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().getString(ORDERID_PARAM);
            id = getArguments().getString(ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("JOB ID " + orderId);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accepted_job_details, container, false);

        // this is for casting views from xml file to java file
        castingViews(view);

        ProgressBarHelper.show(getActivity(), "Fetching Job Details");
        ProviderAcceptedJobFragmentController.getInstance().getAcceptedJobDetailsApiCall(id);
        ProviderAcceptedJobFragmentController.getInstance().setProviderAcceptedJobFragmentControllerListener(this);
        callCustomerBtn.setOnClickListener(this);
        viewCustomerInMapBtn.setOnClickListener(this);
        chatWithCustomerBtn.setOnClickListener(this);
        providerJobDoneBtn.setOnClickListener(this);
        return view;
    }

    private void castingViews(View view) {
        jobNameView = view.findViewById(R.id.accepted_details_jobName);
        jobDateView = view.findViewById(R.id.accepted_details_jobDate);
        providerResponsibilityView = view.findViewById(R.id.accepted_details_ProviderResponsibility);
        customerResponsiblityView = view.findViewById(R.id.accepted_details_customerResponsibility);
        customerNameView = view.findViewById(R.id.acceptedDetails_customerName);
        customerLocationView = view.findViewById(R.id.acceptedDetails_customerLocation);
        viewCustomerInMapBtn = view.findViewById(R.id.viewCustomerMapBtn);
        chatWithCustomerBtn = view.findViewById(R.id.chatWithCustomerBtn);
        callCustomerBtn = view.findViewById(R.id.callCustomerBtn);
        providerJobDoneBtn = view.findViewById(R.id.providerJobDoneBtn);
    }

    @Override
    public void onProviderAcceptedSuccessResponse(List<ProviderAcceptedJobModel> providerAcceptedJobModelList) {

    }

    @SuppressLint("SetTextI18n")
    private void setTextToFields(ProviderAcceptedJobModel providerAcceptedJobModel) {
        jobNameView.setText(providerAcceptedJobModel.getServiceTitle());
        jobDateView.setText(providerAcceptedJobModel.getDateAndTime());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            providerResponsibilityView.setText(Html.fromHtml(providerAcceptedJobModel.getProviderResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            providerResponsibilityView.setText(Html.fromHtml(providerAcceptedJobModel.getProviderResponsibility()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            customerResponsiblityView.setText(Html.fromHtml(providerAcceptedJobModel.getCustomerResponsibility(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            customerResponsiblityView.setText(Html.fromHtml(providerAcceptedJobModel.getCustomerResponsibility()));
        }
        customerNameView.setText(providerAcceptedJobModel.getGender() + " " + providerAcceptedJobModel.getName());
        customerLocationView.setText(providerAcceptedJobModel.getLocality());
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onProviderAcceptedFailureReason(String failureReason) {
        Utils.getInstance().showSnackBarOnProviderScreen(failureReason, getActivity());
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onProviderAcceptedDetailsSuccessResponse(ProviderAcceptedJobModel providerAcceptedJobModel) {
        setTextToFields(providerAcceptedJobModel);
        this.acceptedJobModel = providerAcceptedJobModel;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callCustomerBtn:
                if (acceptedJobModel != null) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + acceptedJobModel.getPhoneNumber()));
                    startActivity(callIntent);
                }
                break;
            case R.id.viewCustomerMapBtn:
                if (acceptedJobModel != null) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + acceptedJobModel.getLatitude()
                            + "," + acceptedJobModel.getLongitude());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
                break;
            case R.id.chatWithCustomerBtn:
                Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                chatIntent.putExtra("orderId", id);
                chatIntent.putExtra("userType", "provider");
                chatIntent.putExtra("name", acceptedJobModel.getName());
                startActivity(chatIntent);
                //   Utils.getInstance().showSnackBarOnProviderScreen("Chatting Under Maintainance \nInconvenience Regretted"
                //           , getActivity());
                break;
            case R.id.providerJobDoneBtn:
                createJobDoneAlertDialog();
                break;
        }
    }

    private void createJobDoneAlertDialog() {
        ViewGroup viewGroup = getActivity().findViewById(R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_provider_job_done, viewGroup, false);

        TextView jobDoneBtn = dialogView.findViewById(R.id.alertcompleteJobDoneBtn);
        EditText extraAmountEditText = dialogView.findViewById(R.id.alertJobPriceEditText);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.alert_background));

        jobDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extraAmount = extraAmountEditText.getText().toString();
                if (extraAmount.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Amount as Zero if No Extra Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                ProgressBarHelper.show(getActivity(), "Completing Job");
                callJobCompleteApiCall(alertDialog);
            }
        });
    }

    private void callJobCompleteApiCall(AlertDialog alertDialog) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> jobCompleteCall = goBuddyApiInterface.providerCompleteJob(createproviderJobCompleteMap());
        jobCompleteCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            onSuccessResponse(jsonObject.getString("message"), alertDialog);
                        } else {
                            onFailureResponse(jsonObject.getString("message"), alertDialog);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                ProgressBarHelper.dismiss(getActivity());
                onFailureResponse(t.getLocalizedMessage(), alertDialog);
            }
        });
    }

    private void onFailureResponse(String message, AlertDialog alertDialog) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnProviderScreen(message, getActivity());
    }

    private void onSuccessResponse(String message, AlertDialog alertDialog) {
        alertDialog.dismiss();
        ProgressBarHelper.dismiss(getActivity());
        ((ProviderMainActivity) getActivity()).changeProviderHomeSelection(R.id.provider_completed_jobs);
        Utils.getInstance().showSnackBarOnProviderScreen(message, getActivity());
    }

    private Map<String, String> createproviderJobCompleteMap() {
        Map<String, String> jobCompleteMap = new HashMap<>();
        jobCompleteMap.put("extra_amount", extraAmount);
        jobCompleteMap.put("order_id", id);
        return jobCompleteMap;
    }

}
