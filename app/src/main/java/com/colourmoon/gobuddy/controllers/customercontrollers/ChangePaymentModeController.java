package com.colourmoon.gobuddy.controllers.customercontrollers;

import androidx.annotation.NonNull;

import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiClient;
import com.colourmoon.gobuddy.serverinteractions.GoBuddyApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePaymentModeController {
    private ChangePaymentModeController() {
        //private constructor
    }

    private static ChangePaymentModeController paymentModeController;

    public static ChangePaymentModeController getInstance() {
        if (paymentModeController == null) {
            paymentModeController = new ChangePaymentModeController();
        }
        return paymentModeController;
    }

    public interface ChangePaymentControllerListener {
        void onChangePaymentModeSuccess(String successResponse);

        void onChangePaymentModeFailure(String failureResponse);
    }

    private ChangePaymentControllerListener changePaymentControllerListener;

    public void setChangePaymentControllerListener(ChangePaymentControllerListener changePaymentControllerListener) {
        this.changePaymentControllerListener = changePaymentControllerListener;
    }

    public void changePaymentModeApiCall(Map<String, String> changePaymentModeMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> addOrderCall = goBuddyApiInterface.changePaymentMode(changePaymentModeMap);
        addOrderCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equals("valid")) {
                            if (changePaymentControllerListener != null) {
                                changePaymentControllerListener.onChangePaymentModeSuccess(jsonObject.getString("message"));
                            }
                        } else {
                            if (changePaymentControllerListener != null) {
                                changePaymentControllerListener.onChangePaymentModeFailure(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (changePaymentControllerListener != null) {
                        changePaymentControllerListener.onChangePaymentModeFailure("No Response From Server \nTry Again");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (changePaymentControllerListener != null) {
                    changePaymentControllerListener.onChangePaymentModeFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
