package com.colourmoon.gobuddy.controllers.providercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.model.PayoutModel;
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

public class SettledPayoutsFragmentController {

    private SettledPayoutsFragmentController() {
        // private constructor
    }

    private static SettledPayoutsFragmentController settledPayoutsFragmentController;

    public static SettledPayoutsFragmentController getInstance() {
        if (settledPayoutsFragmentController == null) {
            settledPayoutsFragmentController = new SettledPayoutsFragmentController();
        }
        return settledPayoutsFragmentController;
    }

    public interface SettledPayoutsFragmentControllerListener {
        void onSettledSuccessResponse(List<PayoutModel> payoutModelList);

        void onSettledFailureResponse(String failureReason);
    }

    private SettledPayoutsFragmentControllerListener settledPayoutsFragmentControllerListener;

    public void setSettledPayoutsFragmentControllerListener(SettledPayoutsFragmentControllerListener settledPayoutsFragmentControllerListener) {
        this.settledPayoutsFragmentControllerListener = settledPayoutsFragmentControllerListener;
    }

    public void getSettledPayoutsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getPayoutsCall = goBuddyApiInterface.getSettledPayouts(userId);
        getPayoutsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String settledListString = jsonObject.getString("list");
                            JSONArray settledJsonArray = new JSONArray(settledListString);
                            List<PayoutModel> payoutModelList = new ArrayList<>();
                            for (int i = 0; i < settledJsonArray.length(); i++) {
                                JSONObject settledJsonObject = settledJsonArray.getJSONObject(i);
                                payoutModelList.add(new PayoutModel(
                                        settledJsonObject.getString("job_id"),
                                        settledJsonObject.getString("job_name"),
                                        settledJsonObject.getString("date_time"),
                                        settledJsonObject.getString("amount")
                                ));
                            }
                            if (settledPayoutsFragmentControllerListener != null) {
                                settledPayoutsFragmentControllerListener.onSettledSuccessResponse(payoutModelList);
                            }
                        } else {
                            if (settledPayoutsFragmentControllerListener != null) {
                                settledPayoutsFragmentControllerListener.onSettledFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (settledPayoutsFragmentControllerListener != null) {
                        settledPayoutsFragmentControllerListener.onSettledFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (settledPayoutsFragmentControllerListener != null) {
                    settledPayoutsFragmentControllerListener.onSettledFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

}
