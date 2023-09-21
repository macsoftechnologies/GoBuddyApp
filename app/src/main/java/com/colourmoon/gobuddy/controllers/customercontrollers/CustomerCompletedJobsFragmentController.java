package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ProviderAcceptedJobModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCompletedJobsFragmentController {
    private CustomerCompletedJobsFragmentController() {
        // private constructor
    }

    private static CustomerCompletedJobsFragmentController customerCompletedJobsFragmentController;

    public static CustomerCompletedJobsFragmentController getInstance() {
        if (customerCompletedJobsFragmentController == null) {
            customerCompletedJobsFragmentController = new CustomerCompletedJobsFragmentController();
        }
        return customerCompletedJobsFragmentController;
    }

    public interface CustomerCompletedJobsFragmentControllerListener {
        void onCustomerCompletedJonsSuccessResponse(ProviderAcceptedJobModel providerAcceptedJobModel);

        void onCustomerCompletedJobsFailureResponse(String failureReason);
    }

    private CustomerCompletedJobsFragmentControllerListener customerCompletedJobsFragmentControllerListener;

    public void setCustomerCompletedJobsFragmentControllerListener(CustomerCompletedJobsFragmentControllerListener customerCompletedJobsFragmentControllerListener) {
        this.customerCompletedJobsFragmentControllerListener = customerCompletedJobsFragmentControllerListener;
    }

    public void getCustomerCompletedJobDetailsApiCall(String orderId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> acceptedJobsCall = goBuddyApiInterface.getOnGoingOrderDetails(orderId);
        acceptedJobsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String acceptedJobsString = jsonObject.getString("job_details");
                            if (!acceptedJobsString.isEmpty()) {
                                JSONObject acceptedDetailsJsonObject = new JSONObject(acceptedJobsString);
                                ProviderAcceptedJobModel providerAcceptedJobModel;
                                providerAcceptedJobModel = new ProviderAcceptedJobModel(
                                        acceptedDetailsJsonObject.getString("id"),
                                        acceptedDetailsJsonObject.getString("order_id"),
                                        acceptedDetailsJsonObject.getString("service_date"),
                                        acceptedDetailsJsonObject.getString("service_time"),
                                        acceptedDetailsJsonObject.getString("title"),
                                        acceptedDetailsJsonObject.getString("sstitle"),
                                        acceptedDetailsJsonObject.getString("presponsibility"),
                                        acceptedDetailsJsonObject.getString("cresponsibility"),
                                        acceptedDetailsJsonObject.getString("note"),
                                        acceptedDetailsJsonObject.getString("house_street"),
                                        acceptedDetailsJsonObject.getString("locality"),
                                        acceptedDetailsJsonObject.getString("gender"),
                                        acceptedDetailsJsonObject.getString("name"),
                                        acceptedDetailsJsonObject.getString("latitude"),
                                        acceptedDetailsJsonObject.getString("longitude"),
                                        acceptedDetailsJsonObject.getString("phone_number"),
                                        acceptedDetailsJsonObject.getString("date_time"),
                                        acceptedDetailsJsonObject.getString("order_status"),
                                        acceptedDetailsJsonObject.getString("customer_confirm"),
                                        acceptedDetailsJsonObject.getString("payment_mode"),
                                        acceptedDetailsJsonObject.getString("total_amount")
                                );

                                if (customerCompletedJobsFragmentControllerListener != null) {
                                    customerCompletedJobsFragmentControllerListener.onCustomerCompletedJonsSuccessResponse(providerAcceptedJobModel);
                                }
                            } else {
                                if (customerCompletedJobsFragmentControllerListener != null) {
                                    customerCompletedJobsFragmentControllerListener.onCustomerCompletedJobsFailureResponse("Job Details Not Found");
                                }
                            }
                        } else {
                            if (customerCompletedJobsFragmentControllerListener != null) {
                                customerCompletedJobsFragmentControllerListener.onCustomerCompletedJobsFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (customerCompletedJobsFragmentControllerListener != null) {
                        customerCompletedJobsFragmentControllerListener.onCustomerCompletedJobsFailureResponse("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (customerCompletedJobsFragmentControllerListener != null) {
                    customerCompletedJobsFragmentControllerListener.onCustomerCompletedJobsFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

}
