package com.colourmoon.gobuddy;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.controllers.providercontrollers.ProviderAcceptedJobFragmentController;
import com.colourmoon.gobuddy.model.ProviderAcceptedJobModel;
import com.colourmoon.gobuddy.model.ProviderAcceptedJobModels;
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

public class ProviderAcceptedFragmentController {

    private ProviderAcceptedFragmentController() {
        // private constructor
    }

    private static ProviderAcceptedFragmentController providerAcceptedFragmentController;

    public static ProviderAcceptedFragmentController getInstance() {
        if (providerAcceptedFragmentController == null) {
            providerAcceptedFragmentController = new ProviderAcceptedFragmentController();
        }
        return providerAcceptedFragmentController;
    }

    public interface ProviderAcceptedFragmentControllerListener {
        void onProviderAcceptedSuccessResponse(List<ProviderAcceptedJobModels> providerAcceptedJobModelList);

        void onProviderAcceptedFailureReason(String failureReason);

      //  void onProviderAcceptedDetailsSuccessResponse(ProviderAcceptedJobModel providerAcceptedJobModel);
    }

    private ProviderAcceptedFragmentController.ProviderAcceptedFragmentControllerListener providerAcceptedFragmentControllerListener;

    public void setProviderAcceptedFragmentControllerListener(ProviderAcceptedFragmentController.ProviderAcceptedFragmentControllerListener providerAcceptedFragmentControllerListener) {
        this.providerAcceptedFragmentControllerListener = providerAcceptedFragmentControllerListener;
    }

    public void getProviderAcceptedJobsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> acceptedJobsCall = goBuddyApiInterface.getProviderAcceptedJobs(userId);
        acceptedJobsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String acceptedJobsString = jsonObject.getString("accepted_jobs");
                            if (!acceptedJobsString.isEmpty()) {
                                JSONArray jsonArray = new JSONArray(acceptedJobsString);
                                List<ProviderAcceptedJobModels> providerAcceptedJobModelsList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobJSONObject = jsonArray.getJSONObject(i);
                                    providerAcceptedJobModelsList.add(new ProviderAcceptedJobModels(
                                            jobJSONObject.getString("id"),
                                            jobJSONObject.getString("order_id"),
                                            jobJSONObject.getString("service_date"),
                                            jobJSONObject.getString("service_time"),
                                            jobJSONObject.getString("stitle"),
                                            jobJSONObject.getString("sstitle"),
                                            jobJSONObject.getString("house_street"),
                                            jobJSONObject.getString("locality"),
                                            jobJSONObject.getString("date_time"),
                                            jobJSONObject.getString("total"),
                                            jobJSONObject.getString("payment_mode"),
                                            jsonObject.getString("remaining_amount"),
                                            jobJSONObject.getString("paid_amount")
//                                            jsonObject.getString("coupon_amount"),
//                                            jsonObject.getString("name"),
//                                            jobJSONObject.getString("sub_total")

                                    ));
                                }
                                if (providerAcceptedFragmentControllerListener != null) {
                                    providerAcceptedFragmentControllerListener.onProviderAcceptedSuccessResponse(providerAcceptedJobModelsList);
                                }
                            } else {
                                if (providerAcceptedFragmentControllerListener != null) {
                                    providerAcceptedFragmentControllerListener.onProviderAcceptedFailureReason("No Jobs Found");
                                }
                            }
                        } else {
                            if (providerAcceptedFragmentControllerListener != null) {
                                providerAcceptedFragmentControllerListener.onProviderAcceptedFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (providerAcceptedFragmentControllerListener != null) {
                        providerAcceptedFragmentControllerListener.onProviderAcceptedFailureReason("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (providerAcceptedFragmentControllerListener != null) {
                    providerAcceptedFragmentControllerListener.onProviderAcceptedFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
