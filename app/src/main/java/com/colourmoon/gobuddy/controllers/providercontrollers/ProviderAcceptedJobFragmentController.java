package com.colourmoon.gobuddy.controllers.providercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.ProviderAcceptedJobModel;
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

public class ProviderAcceptedJobFragmentController {

    private ProviderAcceptedJobFragmentController() {
        // private constructor
    }

    private static ProviderAcceptedJobFragmentController providerAcceptedJobFragmentController;

    public static ProviderAcceptedJobFragmentController getInstance() {
        if (providerAcceptedJobFragmentController == null) {
            providerAcceptedJobFragmentController = new ProviderAcceptedJobFragmentController();
        }
        return providerAcceptedJobFragmentController;
    }

    public interface ProviderAcceptedJobFragmentControllerListener {
        void onProviderAcceptedSuccessResponse(List<ProviderAcceptedJobModel> providerAcceptedJobModelList);

        void onProviderAcceptedFailureReason(String failureReason);

        void onProviderAcceptedDetailsSuccessResponse(ProviderAcceptedJobModel providerAcceptedJobModel);
    }

    private ProviderAcceptedJobFragmentControllerListener providerAcceptedJobFragmentControllerListener;

    public void setProviderAcceptedJobFragmentControllerListener(ProviderAcceptedJobFragmentControllerListener providerAcceptedJobFragmentControllerListener) {
        this.providerAcceptedJobFragmentControllerListener = providerAcceptedJobFragmentControllerListener;
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
                                List<ProviderAcceptedJobModel> providerAcceptedJobModelsList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobJSONObject = jsonArray.getJSONObject(i);
                                    providerAcceptedJobModelsList.add(new ProviderAcceptedJobModel(
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
                                            jobJSONObject.getString("payment_mode")
                                    ));
                                }
                                if (providerAcceptedJobFragmentControllerListener != null) {
                                    providerAcceptedJobFragmentControllerListener.onProviderAcceptedSuccessResponse(providerAcceptedJobModelsList);
                                }
                            } else {
                                if (providerAcceptedJobFragmentControllerListener != null) {
                                    providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason("No Jobs Found");
                                }
                            }
                        } else {
                            if (providerAcceptedJobFragmentControllerListener != null) {
                                providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (providerAcceptedJobFragmentControllerListener != null) {
                        providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (providerAcceptedJobFragmentControllerListener != null) {
                    providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getAcceptedJobDetailsApiCall(String orderId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> acceptedJobsCall = goBuddyApiInterface.getProviderAcceptedJobDetails(orderId);
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
                                        acceptedDetailsJsonObject.getString("total"),
                                        acceptedDetailsJsonObject.getString("payment_mode")

                                );

                                if (providerAcceptedJobFragmentControllerListener != null) {
                                    providerAcceptedJobFragmentControllerListener.onProviderAcceptedDetailsSuccessResponse(providerAcceptedJobModel);
                                }
                            } else {
                                if (providerAcceptedJobFragmentControllerListener != null) {
                                    providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason("Job Details Not Found");
                                }
                            }
                        } else {
                            if (providerAcceptedJobFragmentControllerListener != null) {
                                providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (providerAcceptedJobFragmentControllerListener != null) {
                        providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason("No Response From Server\nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (providerAcceptedJobFragmentControllerListener != null) {
                    providerAcceptedJobFragmentControllerListener.onProviderAcceptedFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
