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

public class PayoutsFragmentController {

    private PayoutsFragmentController() {
        // private constructor
    }

    private static PayoutsFragmentController payoutsFragmentController;

    public static PayoutsFragmentController getInstance() {
        if (payoutsFragmentController == null) {
            payoutsFragmentController = new PayoutsFragmentController();
        }
        return payoutsFragmentController;
    }

    public interface PayoutFragmentControllerListener {

        void onNotSettledPayoutSuccessResponse(List<PayoutModel> payoutModelList);

        void onFailureReason(String failureReason);
    }

    private PayoutFragmentControllerListener payoutFragmentControllerListener;

    public void setPayoutFragmentControllerListener(PayoutFragmentControllerListener payoutFragmentControllerListener) {
        this.payoutFragmentControllerListener = payoutFragmentControllerListener;
    }

    public void getNotSettledPayoutsApiCall(String userId) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> getPayoutsCall = goBuddyApiInterface.getNotSettledPayouts(userId);
        getPayoutsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            String onSettledListString = jsonObject.getString("list");
                            JSONArray onSettledJsonArray = new JSONArray(onSettledListString);
                            List<PayoutModel> payoutModelList = new ArrayList<>();
                            for (int i = 0; i < onSettledJsonArray.length(); i++) {
                                JSONObject onSettledJsonObject = onSettledJsonArray.getJSONObject(i);
                                payoutModelList.add(new PayoutModel(
                                        onSettledJsonObject.getString("job_id"),
                                        onSettledJsonObject.getString("job_name"),
                                        onSettledJsonObject.getString("date_time"),
                                        onSettledJsonObject.getString("amount")
                                ));
                            }
                            if (payoutFragmentControllerListener != null) {
                                payoutFragmentControllerListener.onNotSettledPayoutSuccessResponse(payoutModelList);
                            }
                        } else {
                            if (payoutFragmentControllerListener != null) {
                                payoutFragmentControllerListener.onFailureReason(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (payoutFragmentControllerListener != null) {
                        payoutFragmentControllerListener.onFailureReason("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (payoutFragmentControllerListener != null) {
                    payoutFragmentControllerListener.onFailureReason(t.getLocalizedMessage());
                }
            }
        });
    }
}
