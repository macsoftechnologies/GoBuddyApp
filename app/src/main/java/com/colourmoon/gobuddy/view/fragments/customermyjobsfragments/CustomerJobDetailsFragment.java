package com.colourmoon.gobuddy.view.fragments.customermyjobsfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.CustomerJobModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.RotationRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerJobDetailsFragment extends Fragment {

    private static final String CUSTOMERJOB_ID_PARAM = "customerJobId";

    private CustomerJobModel customerJobModel;
    private String jobId,ratingStr;

    public CustomerJobDetailsFragment() {
        // Required empty public constructor
    }

    // widgets
    private TextView jobNameView, jobDateView, jobStatusView, jobAddressView, jobTitleView, jobPriceView, jobBookingAmountView,
            jobExtraAmountView, jobInvoiceAmountView, jobAmountPaidView, providerNameView, jobDetailsHelpBtn,submit_btn;
    private CircleImageView jobDetaislProvider_imageView;
    private RotationRatingBar jobDetails_ratingBar;
    private EditText edtComment;

    public static CustomerJobDetailsFragment newInstance(String customerJobId) {
        CustomerJobDetailsFragment fragment = new CustomerJobDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CUSTOMERJOB_ID_PARAM, customerJobId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobId = getArguments().getString(CUSTOMERJOB_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_job_details, container, false);

        // this is for casting views from xml file to java file
        castingViews(view);

        ProgressBarHelper.show(getActivity(), "Fetching Order Details");
        getJobDetailsApiCall();

        //TODO
        jobDetails_ratingBar.setOnRatingChangeListener((ratingBar, rating, fromUser) -> {
            if (!fromUser) return;
            ratingStr=String.valueOf(rating);
        });

        submit_btn.setOnClickListener(view1 -> {
            if (customerJobModel.getRating().equals("Given")) {
                Utils.getInstance().showSnackBarOnCustomerScreen("You Have Already Submitted Rating", getActivity());
            } else {
                Map<String, String> ratingMap = new HashMap<>();
                ratingMap.put("user_id", UserSessionManagement.getInstance(getActivity()).getUserId());
                ratingMap.put("order_id", customerJobModel.getJobId());
                ratingMap.put("rating", ratingStr);
                ratingMap.put("comments", edtComment.getText().toString());
                postRatingApiCall(ratingMap);
            }
        });
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setTextToFields(String price, String subPrice, String bookingAmount, String ratingValue, String address) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(customerJobModel.getServiceTitle());
        jobNameView.setText(customerJobModel.getServiceTitle());
        jobDateView.setText(customerJobModel.getServiceDate() + ", " + customerJobModel.getServiceTime());
        jobStatusView.setText("Completed");
        jobAddressView.setText(address);
        jobTitleView.setText(customerJobModel.getServiceTitle());
        jobPriceView.setText(getResources().getString(R.string.indian_rupee) + (subPrice.equals("null") ? price : subPrice));
        jobBookingAmountView.setText(getResources().getString(R.string.indian_rupee) + bookingAmount);
        jobExtraAmountView.setText(getResources().getString(R.string.indian_rupee) + customerJobModel.getExtraAmount());
        jobInvoiceAmountView.setText(getResources().getString(R.string.indian_rupee) + customerJobModel.getTotalAmount());
        jobAmountPaidView.setText(getResources().getString(R.string.indian_rupee) + customerJobModel.getTotalAmount());
        providerNameView.setText(customerJobModel.getProviderName());
        edtComment.setText(customerJobModel.getComments());
        Glide.with(getActivity()).load(customerJobModel.getProviderProfileImage()).into(jobDetaislProvider_imageView);
        jobDetails_ratingBar.setRating(Float.parseFloat(ratingValue));

        if (customerJobModel.getRating().equalsIgnoreCase("Given")) {
            jobDetails_ratingBar.setScrollable(false);
            jobDetails_ratingBar.setClickable(false);
        }
        ProgressBarHelper.dismiss(getActivity());
    }

    private void castingViews(View view) {
        jobNameView = view.findViewById(R.id.onGoingJobDetails_Name);
        jobDateView = view.findViewById(R.id.onGoingJobDetaisl_DateAndTime);
        jobStatusView = view.findViewById(R.id.onGoingJobDetails_Status);
        jobAddressView = view.findViewById(R.id.onGoingJobDetails_Address);
        jobTitleView = view.findViewById(R.id.onGoingjobDetails_TitleView);
        jobPriceView = view.findViewById(R.id.onGoingjobDetails_PriceView);
        jobBookingAmountView = view.findViewById(R.id.jobDetails_bookingCostView);
        jobExtraAmountView = view.findViewById(R.id.jobDetails_extraCostView);
        jobInvoiceAmountView = view.findViewById(R.id.jobDetails_invoiceCostView);
        jobAmountPaidView = view.findViewById(R.id.jobDetails_paidCostView);
        providerNameView = view.findViewById(R.id.jobDetails_providerName);
        jobDetailsHelpBtn = view.findViewById(R.id.onGoingJobDetails_HelpBtn);
        jobDetaislProvider_imageView = view.findViewById(R.id.jobDetails_providerImage);
        jobDetails_ratingBar = view.findViewById(R.id.jobDetails_providerRatingBar);
        edtComment = view.findViewById(R.id.edtComment);
        submit_btn = view.findViewById(R.id.submit_btn);

    }

    private void getJobDetailsApiCall() {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> jobDetailsCall = goBuddyApiInterface.getCompletedOrderDetails(jobId);
        jobDetailsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("getJobDetailsApi", response.toString());
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String onGoingJobsString = jsonObject.getString("completed_order_details");
                            JSONObject onGoingJsonObject = new JSONObject(onGoingJobsString);
                            customerJobModel = new CustomerJobModel(
                                    onGoingJsonObject.getString("id"),
                                    onGoingJsonObject.getString("order_id"),
                                    onGoingJsonObject.getString("service_date"),
                                    onGoingJsonObject.getString("service_time"),
                                    onGoingJsonObject.getString("provider_id"),
                                    onGoingJsonObject.getString("stitle"),
                                    onGoingJsonObject.getString("sstitle"),
                                    onGoingJsonObject.getString("provider_profile"),
                                    onGoingJsonObject.getString("provider_name"),
                                    onGoingJsonObject.getString("rating"),

                                    "",
                                    onGoingJsonObject.getString("total_amount"),
                                    onGoingJsonObject.getString("extra_amount"),
                                    onGoingJsonObject.getString("order_status"),
                                    onGoingJsonObject.getString("customer_confirm"),
                                    "", "",
                                    onGoingJsonObject.getString("comments")

                                    );
                            setTextToFields(onGoingJsonObject.getString("sprice"),
                                    onGoingJsonObject.getString("ssprice"),
                                    onGoingJsonObject.getString("sub_total"),
                                    onGoingJsonObject.getString("rating_value"),
                                    onGoingJsonObject.getString("house_street"));

                        } else {
                            ProgressBarHelper.dismiss(getActivity());
                            Utils.getInstance().showSnackBarOnCustomerScreen(jsonObject.getString("message"), getActivity());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ProgressBarHelper.dismiss(getActivity());
                    Utils.getInstance().showSnackBarOnCustomerScreen("No Response From Server", getActivity());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                ProgressBarHelper.dismiss(getActivity());
                Utils.getInstance().showSnackBarOnCustomerScreen(t.getLocalizedMessage(), getActivity());
            }
        });
    }

    private void postRatingApiCall(Map<String, String> ratingMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> jobDetailsCall = goBuddyApiInterface.postRating(ratingMap);
        jobDetailsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            createSuccessAlert(ratingMap.get("rating"));
                        } else {
                            Utils.getInstance().showSnackBarOnCustomerScreen(jsonObject.getString("message"), getActivity());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.getInstance().showSnackBarOnCustomerScreen("No Response From Server", getActivity());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Utils.getInstance().showSnackBarOnCustomerScreen(t.getLocalizedMessage(), getActivity());
            }
        });
    }

    private void createSuccessAlert(String rating) {

        ViewGroup viewGroup = getActivity().findViewById(R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_rating_dialog, viewGroup, false);

        TextView ratingView = dialogView.findViewById(R.id.alert_ratingView);
        ratingView.setText(rating);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.alert_background));

        TextView okBtn = dialogView.findViewById(R.id.alert_okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Utils.getInstance().showSnackBarOnCustomerScreen("You Have Successfully Submitted Rating", getActivity());
            }
        });
    }
}


