package com.colourmoon.gobuddy.view.fragments.customermyjobsfragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.PaytmController;
import com.colourmoon.gobuddy.controllers.customercontrollers.ChangePaymentModeController;
import com.colourmoon.gobuddy.controllers.customercontrollers.CustomerCompletedJobsFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.ProviderAcceptedJobModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.activities.ChatActivity;
import com.colourmoon.gobuddy.view.alertdialogs.PaymentBottomSheetDialog;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.colourmoon.gobuddy.utilities.Constants.PAYTM_CHANNEL_ID;
import static com.colourmoon.gobuddy.utilities.Constants.PAYTM_INDUSTRY_TYPE;
import static com.colourmoon.gobuddy.utilities.Constants.PAYTM_MERCHANT_ID;
import static com.colourmoon.gobuddy.utilities.Constants.PAYTM_WEBSITE;

public class CustomerOnGoingJobsDetailsFragment extends Fragment implements CustomerCompletedJobsFragmentController.CustomerCompletedJobsFragmentControllerListener, View.OnClickListener, PaytmController.PaytmControllerListener, PaytmPaymentTransactionCallback, PaymentBottomSheetDialog.PaymentBottomSheetDialogListener, ChangePaymentModeController.ChangePaymentControllerListener {

    private static final String ORDERID_PARAM = "orderIdParam";
    private static final String ID_PARAM = "idParam";

    // TODO: Rename and change types of parameters
    private String orderId, id, uniqueOrderId;

    private TextView jobNameView, jobDateView, providerResponsibilityView, customerResponsiblityView,
            customerNameView, customerLocationView, proceedToPaymentBtn;
    private ImageView chatWithCustomerBtn, callCustomerBtn;
    private CardView contactProviderView;

    private ProviderAcceptedJobModel customerOnGoingJobModel;
    private String paymentType;

    public CustomerOnGoingJobsDetailsFragment() {
        // Required empty public constructor
    }

    public static CustomerOnGoingJobsDetailsFragment newInstance(String orderId, String id) {
        CustomerOnGoingJobsDetailsFragment fragment = new CustomerOnGoingJobsDetailsFragment();
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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_on_going_jobs_details, container, false);

        castingViews(view);

        ProgressBarHelper.show(getActivity(), "Getting Job Details");
        CustomerCompletedJobsFragmentController.getInstance().getCustomerCompletedJobDetailsApiCall(orderId);
        CustomerCompletedJobsFragmentController.getInstance().setCustomerCompletedJobsFragmentControllerListener(this);

        callCustomerBtn.setOnClickListener(this);
        chatWithCustomerBtn.setOnClickListener(this);
        proceedToPaymentBtn.setOnClickListener(this);

        return view;
    }

    private void castingViews(View view) {
        jobNameView = view.findViewById(R.id.ongoing_job_details_jobName);
        jobDateView = view.findViewById(R.id.ongoing_job_details_jobDate);
        providerResponsibilityView = view.findViewById(R.id.ongoing_job_details_ProviderResponsibility);
        customerResponsiblityView = view.findViewById(R.id.ongoing_job_details_customerResponsibility);
        customerNameView = view.findViewById(R.id.ongoing_job_Details_customerName);
        customerLocationView = view.findViewById(R.id.ongoing_job_Details_customerLocation);
        chatWithCustomerBtn = view.findViewById(R.id.ongoing_job_chatWithCustomerBtn);
        callCustomerBtn = view.findViewById(R.id.ongoing_job_callCustomerBtn);
        contactProviderView = view.findViewById(R.id.contactProviderView);
        proceedToPaymentBtn = view.findViewById(R.id.proceedToPaymentBtn);
    }


    @Override
    public void onCustomerCompletedJonsSuccessResponse(ProviderAcceptedJobModel providerAcceptedJobModel) {
        setTextToFields(providerAcceptedJobModel);
        this.customerOnGoingJobModel = providerAcceptedJobModel;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(providerAcceptedJobModel.getServiceTitle());
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
        if (providerAcceptedJobModel.getOrderStatus().equalsIgnoreCase("1")) {
            contactProviderView.setVisibility(View.GONE);
            proceedToPaymentBtn.setVisibility(View.GONE);
        } else if (providerAcceptedJobModel.getOrderStatus().equalsIgnoreCase("2")) {
            proceedToPaymentBtn.setVisibility(View.GONE);
        }
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onCustomerCompletedJobsFailureResponse(String failureReason) {
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ongoing_job_callCustomerBtn:
                if (customerOnGoingJobModel != null) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + customerOnGoingJobModel.getPhoneNumber()));
                    startActivity(callIntent);
                }
                break;

            case R.id.ongoing_job_chatWithCustomerBtn:
                Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                chatIntent.putExtra("orderId", orderId);
                chatIntent.putExtra("userType", "customer");
                chatIntent.putExtra("name", customerOnGoingJobModel.getName());
                startActivity(chatIntent);
                //  Utils.getInstance().showSnackBarOnCustomerScreen("Chatting Under Maintenance \nInconvenience Regretted"
                //        , getActivity());
                break;
            case R.id.proceedToPaymentBtn:
                PaymentBottomSheetDialog paymentBottomSheetDialog = PaymentBottomSheetDialog.getInstance();
                paymentBottomSheetDialog.setPaymentBottomSheetDialogListener(this);
                paymentBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "paymentBottomSheet");
                break;
        }
    }

    private void createAlertDilaog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Pay by Cash Alert")
                .setIcon(R.drawable.ic_logout_icon)
                .setMessage("Have you paid money to the provider")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                })
                .setPositiveButton("paid", (dialogInterface, i) -> jobDoneByCustomerApiCall(dialogInterface))
        ;
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void jobDoneByCustomerApiCall(DialogInterface dialog) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> jobDoneCall = goBuddyApiInterface.jobDoneByCustomer(orderId);
        jobDoneCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            dialog.dismiss();
                            getActivity().getSupportFragmentManager().popBackStack();
                            addToFragmentContainer(CustomerJobDetailsFragment.newInstance(orderId),
                                    true, "CompletedJobsFragment");
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
                t.printStackTrace();
                Utils.getInstance().showSnackBarOnCustomerScreen(t.getLocalizedMessage(), getActivity());
            }
        });
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
    public void onPaytmSuccessResponse(String checkSumHash) {
        ProgressBarHelper.dismiss(getActivity());
        initializePaytmPayment(checkSumHash);
    }

    @Override
    public void onServerSuccessResponse(String serverResponse) {
        ProgressBarHelper.dismiss(getActivity());
        getActivity().getSupportFragmentManager().popBackStack();
        addToFragmentContainer(CustomerJobDetailsFragment.newInstance(orderId),
                true, "CompletedJobsFragment");
    }

    private void initializePaytmPayment(String checkSumHash) {
        //getting paytm service
        //     PaytmPGService Service = PaytmPGService.getStagingService("");

        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();

        // testing
        //   String PAYTM_CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;
        String PAYTM_CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;

        //creating a hashmap and adding all the values required
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", PAYTM_MERCHANT_ID);
        paramMap.put("ORDER_ID", uniqueOrderId);
        paramMap.put("CUST_ID", UserSessionManagement.getInstance(getActivity()).getUserId());
        paramMap.put("INDUSTRY_TYPE_ID", PAYTM_INDUSTRY_TYPE);
        paramMap.put("CHANNEL_ID", PAYTM_CHANNEL_ID);
        paramMap.put("TXN_AMOUNT", customerOnGoingJobModel.getTotalAmount());
        paramMap.put("WEBSITE", PAYTM_WEBSITE);
        paramMap.put("CALLBACK_URL", PAYTM_CALLBACK_URL);
        paramMap.put("CHECKSUMHASH", checkSumHash);
        Log.d("paytmMap", "" + paramMap.toString());

        //creating a paytm order object using the hashMap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(getActivity(), true, true,
                this);

    }

    @Override
    public void onPaytmFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        Utils.getInstance().showSnackBarOnCustomerScreen(failureReason, getActivity());
        Log.d("paytmResponse", "failed due to" + failureReason);
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Log.d("PaytmResponse", "onTransactionResponse: " + inResponse.toString());
        String txnResponseMessage = inResponse.getString("RESPMSG");
        String txnStatus = inResponse.getString("STATUS");
        String txnId = inResponse.getString("TXNID");
        String txnOrderId = inResponse.getString("ORDERID");
        if (txnStatus.equalsIgnoreCase("TXN_FAILURE")) {
            ProgressBarHelper.show(getActivity(), "Transaction Failed\n Checking With Server");
        } else if (txnStatus.equalsIgnoreCase("TXN_SUCCESS")) {
            ProgressBarHelper.show(getActivity(), "Payment Received\n Checking With Server");
        }
        PaytmController.getInstance().postPaytmResponse(createPaytmResponseMap(txnResponseMessage, txnStatus, txnId, txnOrderId));
    }

    private Map<String, String> createPaytmResponseMap(String txnResponseMessage, String txnStatus, String txnId, String txnOrderId) {
        Map<String, String> paytmResponseMap = new HashMap<>();
        paytmResponseMap.put("TXNAMOUNT", customerOnGoingJobModel.getTotalAmount());
        paytmResponseMap.put("ORDERID", id);
        paytmResponseMap.put("UNIQUE_ORDERID", txnOrderId);
        paytmResponseMap.put("RESPMSG", txnResponseMessage);
        paytmResponseMap.put("STATUS", txnStatus);
        paytmResponseMap.put("TXNID", txnId);
        return paytmResponseMap;
    }

    @Override
    public void networkNotAvailable() {
        Utils.getInstance().showSnackBarOnCustomerScreen("Network Error OCcured", getActivity());
    }

    @Override
    public void onErrorProceed(String s) {
        Toast.makeText(requireActivity(), "" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Utils.getInstance().showSnackBarOnCustomerScreen("Unable To Load WebPage" + inErrorMessage, getActivity());
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Utils.getInstance().showSnackBarOnCustomerScreen("Transaction Cancelled", getActivity());
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Utils.getInstance().showSnackBarOnCustomerScreen("Transaction Cancelled " + inResponse.toString(), getActivity());
    }

    @Override
    public void onEventSelected(String type) {
        paymentType = type;
        Map<String, String> changePaymentModeMap = new HashMap<>();
        changePaymentModeMap.put("order_id", id);
        changePaymentModeMap.put("payment_mode", paymentType);
        ChangePaymentModeController.getInstance().changePaymentModeApiCall(changePaymentModeMap);
        ChangePaymentModeController.getInstance().setChangePaymentControllerListener(this);
    }

    @Override
    public void onChangePaymentModeSuccess(String successResponse) {
        uniqueOrderId = UUID.randomUUID().toString();
        // testing
        //   String PAYTM_CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;
        String PAYTM_CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;
        if (paymentType.equalsIgnoreCase("cash")) {
            createAlertDilaog();
        } else if (paymentType.equalsIgnoreCase("online")) {
            Map<String, String> paytmMap = new HashMap<>();
            paytmMap.put("MID", PAYTM_MERCHANT_ID);
            paytmMap.put("ORDER_ID", uniqueOrderId);
            paytmMap.put("CUST_ID", UserSessionManagement.getInstance(getActivity()).getUserId());
            paytmMap.put("INDUSTRY_TYPE_ID", PAYTM_INDUSTRY_TYPE);
            paytmMap.put("CHANNEL_ID", PAYTM_CHANNEL_ID);
            paytmMap.put("TXN_AMOUNT", customerOnGoingJobModel.getTotalAmount());
            paytmMap.put("WEBSITE", PAYTM_WEBSITE);
            paytmMap.put("CALLBACK_URL", PAYTM_CALLBACK_URL);
            ProgressBarHelper.show(getActivity(), "Redirecting to Payment Gateway");
            PaytmController.getInstance().getChecksumHashApiCall(paytmMap);
            PaytmController.getInstance().setPaytmControllerListener(this);
        }

    }


    @Override
    public void onChangePaymentModeFailure(String failureResponse) {
        Utils.getInstance().showSnackBarOnCustomerScreen(failureResponse, getActivity());
    }
}
