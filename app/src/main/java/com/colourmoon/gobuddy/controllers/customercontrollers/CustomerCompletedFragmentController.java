package com.colourmoon.gobuddy.controllers.customercontrollers;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.CustomerJobModel;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCompletedFragmentController {

    private CustomerCompletedFragmentController() {
        // private constructor
    }

    private static CustomerCompletedFragmentController customerCompletedFragmentController;

    public static CustomerCompletedFragmentController getInstance() {
        if (customerCompletedFragmentController == null) {
            customerCompletedFragmentController = new CustomerCompletedFragmentController();
        }
        return customerCompletedFragmentController;
    }

    public interface CustomerCompletedFragmentControllerListener {
        void onCustomerCompletedJobsResponse(List<CustomerJobModel> customerJobModelList);

        void onMakeFavoriteSuccessResponse(String successResponse, int position);

        void onMakeFavoriteFailureResponse(String failureResponse, int position);

        void onCustomerCompletedJobsFailureResponse(String failureReason);
    }

    private CustomerCompletedFragmentControllerListener customerCompletedFragmentControllerListener;

    public void setCustomerCompletedFragmentControllerListener(CustomerCompletedFragmentControllerListener customerCompletedFragmentControllerListener) {
        this.customerCompletedFragmentControllerListener = customerCompletedFragmentControllerListener;
    }

    public void getCompletedJobsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getOnCompletedJobsCall = goBuddyApiInterface.getCustomerCompletedJobs(userId);
        getOnCompletedJobsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String onGoingJobsString = jsonObject.getString("completed_user_jobs");
                            JSONArray jsonArray = new JSONArray(onGoingJobsString);
                            List<CustomerJobModel> customerJobModels = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject onGoingJsonObject = jsonArray.getJSONObject(i);
                                customerJobModels.add(new CustomerJobModel(
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
                                        onGoingJsonObject.getString("review_count"),
                                        onGoingJsonObject.getString("total_amount"),
                                        onGoingJsonObject.getString("extra_amount"),
                                        onGoingJsonObject.getString("order_status"),
                                        onGoingJsonObject.getString("customer_confirm"),
                                        onGoingJsonObject.getString("payment_mode"),
                                        onGoingJsonObject.getString("favourite"),
                                       ""
                                ));
                            }
                            if (customerCompletedFragmentControllerListener != null) {
                                customerCompletedFragmentControllerListener.onCustomerCompletedJobsResponse(customerJobModels);
                            }
                        } else {
                            if (customerCompletedFragmentControllerListener != null) {
                                customerCompletedFragmentControllerListener.onCustomerCompletedJobsFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (customerCompletedFragmentControllerListener != null) {
                        customerCompletedFragmentControllerListener.onCustomerCompletedJobsFailureResponse("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (customerCompletedFragmentControllerListener != null) {
                    customerCompletedFragmentControllerListener.onCustomerCompletedJobsFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void postFavouritesApiCall(ImageView imageView, String orderId, int position) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getOnCompletedJobsCall = goBuddyApiInterface.postFavourite(orderId);
        getOnCompletedJobsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (customerCompletedFragmentControllerListener != null) {
                                customerCompletedFragmentControllerListener.onMakeFavoriteSuccessResponse(
                                        jsonObject.getString("message"), position);
                            }
                        } else {
                            if (customerCompletedFragmentControllerListener != null) {
                                customerCompletedFragmentControllerListener.onMakeFavoriteFailureResponse(
                                        jsonObject.getString("message"), position);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (customerCompletedFragmentControllerListener != null) {
                        customerCompletedFragmentControllerListener.onMakeFavoriteFailureResponse(
                                "No Response From Server\nTry Again", position);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (customerCompletedFragmentControllerListener != null) {
                    customerCompletedFragmentControllerListener.onMakeFavoriteFailureResponse(
                            t.getLocalizedMessage(), position);
                }
            }
        });
    }
}
