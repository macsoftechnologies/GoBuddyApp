package com.colourmoon.gobuddy.controllers.providercontrollers;

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

public class AcceptJobController {
    private AcceptJobController() {
        // private constructor
    }

    private static AcceptJobController acceptJobController;

    public static AcceptJobController getInstance() {
        if (acceptJobController == null) {
            acceptJobController = new AcceptJobController();
        }
        return acceptJobController;
    }

    public interface AcceptJobControllerListener {
        void onAcceptJobSuccessResponse(String successResponse);

        void onAcceptJobFailureResponse(String failureReason);
    }

    private AcceptJobControllerListener acceptJobControllerListener;

    public void setAcceptJobControllerListener(AcceptJobControllerListener acceptJobControllerListener) {
        this.acceptJobControllerListener = acceptJobControllerListener;
    }

    public void acceptJobApiCall(Map<String, String> acceptJobMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> acceptJobCall = goBuddyApiInterface.acceptJob(acceptJobMap);
        acceptJobCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("acceptJobContResponse", "Triggered");
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.getString("status").equalsIgnoreCase("valid")) {
                            if (acceptJobControllerListener != null) {
                                acceptJobControllerListener.onAcceptJobSuccessResponse(jsonObject.getString("message"));
                            }
                        } else {
                            if (acceptJobControllerListener != null) {
                                acceptJobControllerListener.onAcceptJobFailureResponse(jsonObject.getString("message"));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (acceptJobControllerListener != null) {
                        acceptJobControllerListener.onAcceptJobFailureResponse("No Response From Server");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (acceptJobControllerListener != null) {
                    acceptJobControllerListener.onAcceptJobFailureResponse(t.getLocalizedMessage());
                }
            }
        });
    }

    public void acceptJobNotificationApiCall(Map<String, String> acceptJobNotificationMap) {
        GoBuddyApiInterface goBuddyApiInterface = GoBuddyApiClient.getGoBuddyClient().create(GoBuddyApiInterface.class);
        Call<ResponseBody> acceptJobCall = goBuddyApiInterface.acceptJobNotification(acceptJobNotificationMap);
        acceptJobCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("acceptJobContResponse", "Triggered");
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
