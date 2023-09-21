package com.colourmoon.gobuddy.view.fragments.customermyjobsfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colourmoon.gobuddy.R;
import com.colourmoon.gobuddy.controllers.commoncontrollers.PaytmController;
import com.colourmoon.gobuddy.controllers.customercontrollers.ChangePaymentModeController;
import com.colourmoon.gobuddy.controllers.customercontrollers.OnGoingFragmentController;
import com.colourmoon.gobuddy.helper.ProgressBarHelper;
import com.colourmoon.gobuddy.model.CustomerJobModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;
import com.colourmoon.gobuddy.utilities.UserSessionManagement;
import com.colourmoon.gobuddy.utilities.Utils;
import com.colourmoon.gobuddy.view.adapters.CustomerOnGoingJobsAdapter;
import com.colourmoon.gobuddy.view.alertdialogs.PaymentBottomSheetDialog;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

public class onGoingJobsFragment extends Fragment implements
        CustomerOnGoingJobsAdapter.CustomerOnGoingAdapterOnClickListener,
        OnGoingFragmentController.OnGoingFragmentControllerListener, PaytmController.PaytmControllerListener, PaytmPaymentTransactionCallback, PaymentBottomSheetDialog.PaymentBottomSheetDialogListener, ChangePaymentModeController.ChangePaymentControllerListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String orderId, id, totalAmount, uniqueOrderId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public onGoingJobsFragment() {
        // Required empty public constructor
    }

    public static onGoingJobsFragment newInstance(String param1, String param2) {
        onGoingJobsFragment fragment = new onGoingJobsFragment();
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

    //widgets
    private RecyclerView onGoingJobsRecyclerView;
    private View noJobsView;
    private TextView noJobsTextView;
    private ImageView noJobsImageView;
    // data members
    private CustomerJobModel customerJobModel;
    private String paymentType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_going_jobs, container, false);

        onGoingJobsRecyclerView = view.findViewById(R.id.onGoingJobsRecyclerView);
        noJobsView = view.findViewById(R.id.customer_ongoing_noJobs_view);
        noJobsImageView = noJobsView.findViewById(R.id.emptyImageView);
        noJobsTextView = noJobsView.findViewById(R.id.empty_textView);

        if (UserSessionManagement.getInstance(getActivity()).isLoggedIn()) {
            ProgressBarHelper.show(getActivity(), "Fetching Your OnGoing Jobs");
            OnGoingFragmentController.getInstance().getOnGoingJobsApiCall(UserSessionManagement.getInstance(getActivity()).getUserId());
            OnGoingFragmentController.getInstance().setOnGoingFragmentControllerListener(this);
        } else {
            Utils.getInstance().showSnackBarOnCustomerScreen("Please Login to View OnGoing Jobs", getActivity());
        }

        return view;
    }

    private void createRecyclerView(List<CustomerJobModel> customerJobModelList) {
        onGoingJobsRecyclerView.setHasFixedSize(true);
        onGoingJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CustomerOnGoingJobsAdapter customerOnGoingJobsAdapter = new CustomerOnGoingJobsAdapter(getActivity(),
                customerJobModelList, "onGoing");
        onGoingJobsRecyclerView.setAdapter(customerOnGoingJobsAdapter);
        customerOnGoingJobsAdapter.setCustomerOnGoingAdapterOnClickListener(this);
        ProgressBarHelper.dismiss(getActivity());
    }

    @Override
    public void onOnGoingJobItemClick(CustomerJobModel customerJobModel) {
        addToFragmentContainer(CustomerOnGoingJobsDetailsFragment.newInstance(customerJobModel.getJobId(),
                customerJobModel.getOrderId()), true, "OnGoingJobDetailsFragment");
    }

    @Override
    public void onPayNowItemClick(CustomerJobModel customerJobModel) {
        this.customerJobModel = customerJobModel;
        orderId = customerJobModel.getOrderId();
        id = customerJobModel.getJobId();
        totalAmount = customerJobModel.getTotalAmount();
        PaymentBottomSheetDialog paymentBottomSheetDialog = PaymentBottomSheetDialog.getInstance();
        paymentBottomSheetDialog.setPaymentBottomSheetDialogListener(this);
        paymentBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "paymentBottomSheet");
    }

    @Override
    public void onMakeFavouriteBtnClick(ImageView favouritesBtn, int position) {

    }

    private void createAlertDilaog(String orderId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Pay by Cash Alert")
                .setIcon(R.drawable.ic_logout_icon)
                .setMessage("Have you paid money to the provider")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                })
                .setPositiveButton("paid", (dialogInterface, i) -> jobDoneByCustomerApiCall(dialogInterface, orderId))
        ;
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void jobDoneByCustomerApiCall(DialogInterface dialog, String orderId) {
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
                            // getActivity().getSupportFragmentManager().popBackStack();
                            addToFragmentContainer(CustomerJobDetailsFragment.newInstance(orderId),
                                    true, "CompletedJobsFragment");
                        }
                    } catch (IOException | JSONException e) {
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

    @Override
    public void onGoingJobsSuccessResponse(List<CustomerJobModel> customerJobModelList) {
        noJobsView.setVisibility(View.GONE);
        createRecyclerView(customerJobModelList);
    }

    @Override
    public void onGoingJobsFailureResponse(String failureReason) {
        ProgressBarHelper.dismiss(getActivity());
        noJobsView.setVisibility(View.VISIBLE);
        noJobsImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_provider_payouts_not_settled));
        noJobsTextView.setText(failureReason);
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

    private void initializePaytmPayment(String checkSumHash) {

        //getting paytm service
        // PaytmPGService Service = PaytmPGService.getStagingService("");

        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();

        //testing
        //  String PAYTM_CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;
        String PAYTM_CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;
        //creating a hashmap and adding all the values required
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", PAYTM_MERCHANT_ID);
        paramMap.put("ORDER_ID", uniqueOrderId);
        paramMap.put("CUST_ID", UserSessionManagement.getInstance(getActivity()).getUserId());
        paramMap.put("INDUSTRY_TYPE_ID", PAYTM_INDUSTRY_TYPE);
        paramMap.put("CHANNEL_ID", PAYTM_CHANNEL_ID);
        paramMap.put("TXN_AMOUNT", totalAmount);
        paramMap.put("WEBSITE", PAYTM_WEBSITE);
        paramMap.put("CALLBACK_URL", PAYTM_CALLBACK_URL);
        paramMap.put("CHECKSUMHASH", checkSumHash);
        Log.d("paytmMap", "" + paramMap.toString());

        //creating a paytm order object using the hashMap
        PaytmOrder order = new PaytmOrder(paramMap);

        //initializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(getActivity(), true, true, this);
    }

    @Override
    public void onServerSuccessResponse(String serverResponse) {
        ProgressBarHelper.dismiss(getActivity());
        // getActivity().getSupportFragmentManager().popBackStack();
        addToFragmentContainer(CustomerJobDetailsFragment.newInstance(id),
                true, "CompletedJobsFragment");
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

    private Map<String, String> createPaytmResponseMap(String txnResponseMessage, String txnStatus, String txnId,
                                                       String txnOrderId) {
        Map<String, String> paytmResponseMap = new HashMap<>();
        paytmResponseMap.put("TXNAMOUNT", totalAmount);
        paytmResponseMap.put("ORDERID", orderId);
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
        //  String PAYTM_CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;

        String PAYTM_CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + uniqueOrderId;
        if (paymentType.equalsIgnoreCase("cash")) {
            createAlertDilaog(id);
        } else if (paymentType.equalsIgnoreCase("online")) {
            Map<String, String> paytmMap = new HashMap<>();
            paytmMap.put("MID", PAYTM_MERCHANT_ID);
            paytmMap.put("ORDER_ID", uniqueOrderId);
            paytmMap.put("CUST_ID", UserSessionManagement.getInstance(getActivity()).getUserId());
            paytmMap.put("INDUSTRY_TYPE_ID", PAYTM_INDUSTRY_TYPE);
            paytmMap.put("CHANNEL_ID", PAYTM_CHANNEL_ID);
            paytmMap.put("TXN_AMOUNT", totalAmount);
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
