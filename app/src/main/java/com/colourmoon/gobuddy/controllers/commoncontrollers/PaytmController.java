package com.colourmoon.gobuddy.controllers.commoncontrollers;

import android.util.Log;

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

public class PaytmController {

    private PaytmController() {
        // private Constructor
    }

    private static PaytmController paytmController;

    public static PaytmController getInstance() {
        if (paytmController == null) {
            paytmController = new PaytmController();
        }
        return paytmController;
    }

    public interface PaytmControllerListener {
        void onPaytmSuccessResponse(String checkSumHash);

        void onServerSuccessResponse(String serverResponse);

        void onPaytmFailureResponse(String failureReason);
    }

    private PaytmControllerListener paytmControllerListener;

    public void setPaytmControllerListener(PaytmControllerListener paytmControllerListener) {
        this.paytmControllerListener = paytmControllerListener;
    }

    public void getChecksumHashApiCall(Map<String, String> paytmMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getPaytmClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> checkSumCall = goBuddyApiInterface.getPaytmCheckSumHash(paytmMap);
        checkSumCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (paytmControllerListener != null) {
                            paytmControllerListener.onPaytmSuccessResponse(jsonObject.getString("CHECKSUMHASH"));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (paytmControllerListener != null) {
                        paytmControllerListener.onPaytmFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (paytmControllerListener != null) {
                    paytmControllerListener.onPaytmFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void postPaytmResponse(Map<String, String> paytmResponseMap) {
        Log.d("data", paytmResponseMap.toString());
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> postPaytmCall = goBuddyApiInterface.getPaytmResponse(paytmResponseMap);
        postPaytmCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("data", response.toString());
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            if (paytmControllerListener != null) {
                                paytmControllerListener.onServerSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (paytmControllerListener != null) {
                                paytmControllerListener.onPaytmFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (paytmControllerListener != null) {
                        paytmControllerListener.onPaytmFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (paytmControllerListener != null) {
                    paytmControllerListener.onPaytmFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }
}
