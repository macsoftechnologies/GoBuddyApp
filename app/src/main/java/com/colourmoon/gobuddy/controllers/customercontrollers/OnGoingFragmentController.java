package com.colourmoon.gobuddy.controllers.customercontrollers;

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

public class OnGoingFragmentController {

    private OnGoingFragmentController() {
        // private constructor
    }

    private static OnGoingFragmentController onGoingFragmentController;

    public static OnGoingFragmentController getInstance() {
        if (onGoingFragmentController == null) {
            onGoingFragmentController = new OnGoingFragmentController();
        }
        return onGoingFragmentController;
    }

    public interface OnGoingFragmentControllerListener {
        void onGoingJobsSuccessResponse(List<CustomerJobModel> customerJobModelList);

        void onGoingJobsFailureResponse(String failureReason);
    }

    private OnGoingFragmentControllerListener onGoingFragmentControllerListener;

    public void setOnGoingFragmentControllerListener(OnGoingFragmentControllerListener onGoingFragmentControllerListener) {
        this.onGoingFragmentControllerListener = onGoingFragmentControllerListener;
    }

    public void getOnGoingJobsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getOnGoingJobsCall = goBuddyApiInterface.getCustomerOnGoingJobs(userId);
        getOnGoingJobsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String onGoingJobsString = jsonObject.getString("ongoing_jobs");
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
                                        onGoingJsonObject.getString("order_status"),
                                        onGoingJsonObject.getString("customer_confirm"),
                                        onGoingJsonObject.getString("payment_mode"),
                                        onGoingJsonObject.getString("total_amount"),
                                        ""));
                            }
                            if (onGoingFragmentControllerListener != null) {
                                onGoingFragmentControllerListener.onGoingJobsSuccessResponse(customerJobModels);
                            }
                        } else {
                            if (onGoingFragmentControllerListener != null) {
                                onGoingFragmentControllerListener.onGoingJobsFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (onGoingFragmentControllerListener != null) {
                        onGoingFragmentControllerListener.onGoingJobsFailureResponse("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (onGoingFragmentControllerListener != null) {
                    onGoingFragmentControllerListener.onGoingJobsFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }


}
