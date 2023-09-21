package com.colourmoon.gobuddy.controllers.providercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ProviderAvailableJobModel;
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

public class ProviderAvailableJobFragmentController {

    private ProviderAvailableJobFragmentController() {
        // private constructor
    }

    private static ProviderAvailableJobFragmentController providerAvailableJobFragmentController;

    public static ProviderAvailableJobFragmentController getInstance() {
        if (providerAvailableJobFragmentController == null) {
            providerAvailableJobFragmentController = new ProviderAvailableJobFragmentController();
        }
        return providerAvailableJobFragmentController;
    }

    public interface ProviderAvailableJobFragmentControllerListener {
        void onProviderAvailableSuccessResponse(List<ProviderAvailableJobModel> providerAvailableJobModelList);

        void onProviderAvailableDetailsResponse(ProviderAvailableJobModel providerAvailableJobModel);

        void onProviderAvailableFailureReason(String failureReason);
    }

    public ProviderAvailableJobFragmentControllerListener providerAvailableJobFragmentControllerListener;

    public void setProviderAvailableJobFragmentControllerListener(ProviderAvailableJobFragmentControllerListener providerAvailableJobFragmentControllerListener) {
        this.providerAvailableJobFragmentControllerListener = providerAvailableJobFragmentControllerListener;
    }

    public void getProviderAvailableJobsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> availableJobsCall = goBuddyApiInterface.getProviderAvailableJobs(userId);
        availableJobsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String availableJobsString = jsonObject.getString("available_jobs");
                            if (!availableJobsString.isEmpty()) {
                                JSONArray jsonArray = new JSONArray(availableJobsString);
                                List<ProviderAvailableJobModel> providerAcceptedJobModelsList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobJSONObject = jsonArray.getJSONObject(i);
                                    providerAcceptedJobModelsList.add(new ProviderAvailableJobModel(
                                            jobJSONObject.getString("id"),
                                            jobJSONObject.getString("order_id"),
                                            jobJSONObject.getString("service_date"),
                                            jobJSONObject.getString("service_time"),
                                            jobJSONObject.getString("stitle"),
                                            jobJSONObject.getString("sstitle"),
                                            jobJSONObject.getString("date_time"),
                                            jobJSONObject.getString("payment_mode"),
                                            jobJSONObject.getString("total"),
                                            jobJSONObject.getString("locality")
                                    ));
                                }
                                if (providerAvailableJobFragmentControllerListener != null) {
                                    providerAvailableJobFragmentControllerListener.onProviderAvailableSuccessResponse(providerAcceptedJobModelsList);
                                }
                            } else {
                                if (providerAvailableJobFragmentControllerListener != null) {
                                    providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason("No Jobs Found");
                                }
                            }
                        } else {
                            if (providerAvailableJobFragmentControllerListener != null) {
                                providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (providerAvailableJobFragmentControllerListener != null) {
                        providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (providerAvailableJobFragmentControllerListener != null) {
                    providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getAvailableJobDetailsApiCall(String orderId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> availableJobDetailsCall = goBuddyApiInterface.getProviderAvailableJobDetails(orderId);
        availableJobDetailsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String avaialbleJobsString = jsonObject.getString("job_details");
                            if (!avaialbleJobsString.isEmpty()) {
                                ProviderAvailableJobModel providerAvailableJobModel = null;
                                JSONObject acceptedDetailsJsonObject = new JSONObject(avaialbleJobsString);
                                providerAvailableJobModel = new ProviderAvailableJobModel(
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
                                        acceptedDetailsJsonObject.getString("date_time"),
                                        acceptedDetailsJsonObject.getString("locality"),
                                        acceptedDetailsJsonObject.getString("name"),
                                        acceptedDetailsJsonObject.getString("gender")
                                );

                                if (providerAvailableJobFragmentControllerListener != null) {
                                    providerAvailableJobFragmentControllerListener.onProviderAvailableDetailsResponse(providerAvailableJobModel);
                                }
                            } else {
                                if (providerAvailableJobFragmentControllerListener != null) {
                                    providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason("Job Details Not Found");
                                }
                            }
                        } else {
                            if (providerAvailableJobFragmentControllerListener != null) {
                                providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (providerAvailableJobFragmentControllerListener != null) {
                        providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (providerAvailableJobFragmentControllerListener != null) {
                    providerAvailableJobFragmentControllerListener.onProviderAvailableFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
