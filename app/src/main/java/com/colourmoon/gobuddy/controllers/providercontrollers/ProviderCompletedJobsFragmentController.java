package com.colourmoon.gobuddy.controllers.providercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ProviderCompletedJobModel;
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

public class ProviderCompletedJobsFragmentController {
    private ProviderCompletedJobsFragmentController() {
        // private constructor
    }

    private static ProviderCompletedJobsFragmentController providerCompletedJobsFragmentController;

    public static ProviderCompletedJobsFragmentController getInstance() {
        if (providerCompletedJobsFragmentController == null) {
            providerCompletedJobsFragmentController = new ProviderCompletedJobsFragmentController();
        }
        return providerCompletedJobsFragmentController;
    }

    public interface ProviderCompletedJobsFragmentControllerListener {
        void onProviderCompletedJobsSuccessResponse(List<ProviderCompletedJobModel> providerCompletedJobModelList);

        void onFailureReason(String failureReason);
    }

    private ProviderCompletedJobsFragmentControllerListener providerCompletedJobsFragmentControllerListener;

    public void setProviderCompletedJobsFragmentControllerListener(ProviderCompletedJobsFragmentControllerListener providerCompletedJobsFragmentControllerListener) {
        this.providerCompletedJobsFragmentControllerListener = providerCompletedJobsFragmentControllerListener;
    }

    public void getProviderCompletedJobsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getProviderCompletedJobsCall = goBuddyApiInterface.getProviderCompletedJobs(userId);
        getProviderCompletedJobsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String completedJobsString = jsonObject.getString("completed_jobs");
                            if (completedJobsString.equals("null")) {
                                return;
                            }
                            JSONArray jsonArray = new JSONArray(completedJobsString);
                            List<ProviderCompletedJobModel> providerCompletedJobModelList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject completedJobsJsonObject = jsonArray.getJSONObject(i);
                                providerCompletedJobModelList.add(new ProviderCompletedJobModel(
                                        completedJobsJsonObject.getString("id"),
                                        completedJobsJsonObject.getString("order_id"),
                                        completedJobsJsonObject.getString("service_date"),
                                        completedJobsJsonObject.getString("service_time"),
                                        completedJobsJsonObject.getString("stitle"),
                                        completedJobsJsonObject.getString("sstitle"),
                                        completedJobsJsonObject.getString("sub_total"),
                                        completedJobsJsonObject.getString("extra_amount"),
                                        completedJobsJsonObject.getString("order_status"),
                                        completedJobsJsonObject.getString("total_amount")
                                ));
                            }
                            if (providerCompletedJobsFragmentControllerListener != null) {
                                providerCompletedJobsFragmentControllerListener.onProviderCompletedJobsSuccessResponse(providerCompletedJobModelList);
                            }
                        } else {
                            if (providerCompletedJobsFragmentControllerListener != null) {
                                providerCompletedJobsFragmentControllerListener.onFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (providerCompletedJobsFragmentControllerListener != null) {
                        providerCompletedJobsFragmentControllerListener.onFailureReason("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (providerCompletedJobsFragmentControllerListener != null) {
                    providerCompletedJobsFragmentControllerListener.onFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
